package com.realestate.config;

import com.realestate.entity.*;
import com.realestate.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static volatile boolean initialized = false;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private ViewingRecordRepository viewingRecordRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private VRPanoramaRepository vrPanoramaRepository;

    @Autowired
    private FollowUpRecordRepository followUpRecordRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private MonthlyTargetRepository monthlyTargetRepository;

    @Autowired
    private BuildingDictRepository buildingDictRepository;

    @Override
    public void run(String... args) {
        if (initialized) {
            return;
        }
        try {
            Thread.sleep(500);
            System.out.println("========== 开始初始化数据 ==========");

            long customerCount = customerRepository.count();
            if (customerCount == 0) {
                initCustomers();
                System.out.println("客户数据初始化完成，共插入 8 条");
            } else {
                System.out.println("客户数据已存在，跳过初始化，当前数量: " + customerCount);
            }

            long buildingCount = buildingDictRepository.count();
            List<BuildingDict> buildingList = null;
            if (buildingCount == 0) {
                buildingList = initBuildings();
                System.out.println("楼盘数据初始化完成，共插入 " + buildingList.size() + " 条");
            } else {
                buildingList = buildingDictRepository.findAll();
                System.out.println("楼盘数据已存在，跳过初始化，当前数量: " + buildingCount);
            }

            long propertyCount = propertyRepository.count();
            if (propertyCount == 0) {
                initProperties(buildingList);
                System.out.println("房源数据初始化完成，共插入 8 条");
            } else {
                System.out.println("房源数据已存在，跳过初始化，当前数量: " + propertyCount);
            }

            long viewingCount = viewingRecordRepository.count();
            if (viewingCount == 0) {
                initViewingRecords();
                System.out.println("带看记录数据初始化完成，共插入 10 条");
            } else {
                System.out.println("带看记录数据已存在，跳过初始化，当前数量: " + viewingCount);
            }

            long vrCount = vrPanoramaRepository.count();
            if (vrCount == 0) {
                initVRPanoramas();
                System.out.println("VR全景数据初始化完成，共插入 18 条");
            } else {
                System.out.println("VR全景数据已存在，跳过初始化，当前数量: " + vrCount);
            }

            long subCount = subscriptionRepository.count();
            if (subCount == 0) {
                initSubscriptions();
                System.out.println("认购签约数据初始化完成，共插入 5 条");
            } else {
                System.out.println("认购签约数据已存在，跳过初始化，当前数量: " + subCount);
            }

            long followCount = followUpRecordRepository.count();
            if (followCount == 0) {
                initFollowUpRecords();
                System.out.println("跟进记录数据初始化完成，共插入 15 条");
            } else {
                System.out.println("跟进记录数据已存在，跳过初始化，当前数量: " + followCount);
            }

            long agentCount = agentRepository.count();
            if (agentCount == 0) {
                initAgents();
                System.out.println("经纪人数据初始化完成，共插入 6 条");
            } else {
                System.out.println("经纪人数据已存在，跳过初始化，当前数量: " + agentCount);
            }

            long targetCount = monthlyTargetRepository.count();
            if (targetCount == 0) {
                initMonthlyTargets();
                System.out.println("月度目标数据初始化完成，共插入 6 条");
            } else {
                System.out.println("月度目标数据已存在，跳过初始化，当前数量: " + targetCount);
            }

            initialized = true;
            System.out.println("========== 数据初始化全部完成 ==========");
        } catch (Exception e) {
            System.err.println("数据初始化异常: " + e.getMessage());
            initialized = true;
        }
    }

    private void initCustomers() {
        String[] names = {"张三", "李四", "王五", "赵六", "钱七", "孙八", "周九", "吴十"};
        String[] phones = {"13800138001", "13800138002", "13800138003", "13800138004",
                          "13800138005", "13800138006", "13800138007", "13800138008"};
        String[] statuses = {"跟进中", "已成交", "已流失", "意向客户"};
        String[] levels = {"A类", "B类", "C类", "D类"};
        String[] sources = {"线上咨询", "朋友介绍", "门店来访", "电话咨询"};

        for (int i = 0; i < 8; i++) {
            Customer c = new Customer();
            c.setName(names[i]);
            c.setPhone(phones[i]);
            c.setGender(i % 2 == 0 ? "男" : "女");
            c.setAge(25 + i * 5);
            c.setIdCard("110101199" + i + "0101" + (1000 + i));
            c.setAddress("北京市朝阳区" + (i + 1) + "号小区");
            c.setBudget((200 + i * 50) + "万");
            c.setPreferredArea("朝阳区");
            c.setPreferredType(i % 2 == 0 ? "住宅" : "公寓");
            c.setStatus(statuses[i % 4]);
            c.setSource(sources[i % 4]);
            c.setLevel(levels[i % 4]);
            c.setRemark("客户备注信息" + (i + 1));
            customerRepository.save(c);
        }
    }

    private void initProperties(List<BuildingDict> buildingList) {
        String[] titles = {"朝阳公园旁精装三居", "国贸CBD豪华公寓", "望京SOHO精品两居", "海淀学区房三居",
                          "通州地铁口新房", "亦庄开发区精装四居", "昌平别墅区独栋", "石景山刚需两居"};
        String[] types = {"住宅", "公寓", "住宅", "住宅", "住宅", "住宅", "别墅", "住宅"};
        String[] addresses = {"朝阳区朝阳公园南路1号", "朝阳区建国门外大街1号", "朝阳区望京街9号",
                             "海淀区中关村大街1号", "通州区新华大街1号", "大兴区亦庄荣华路1号",
                             "昌平区回龙观别墅区1号", "石景山区古城大街1号"};
        String[] cities = {"北京", "北京", "北京", "北京", "北京", "北京", "北京", "北京"};
        String[] districts = {"朝阳区", "朝阳区", "朝阳区", "海淀区", "通州区", "大兴区", "昌平区", "石景山区"};
        int[] prices = {680, 1200, 450, 850, 320, 520, 1500, 280};
        int[] rooms = {3, 2, 2, 3, 2, 4, 5, 2};
        int[] halls = {2, 2, 1, 2, 1, 2, 3, 1};
        Long[] buildingIds = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L};
        String[] buildingNames = {"朝阳公园花园", "国贸中心公寓", "望京新城", "海淀学府花园",
                                  "通州新华家园", "亦庄林语墅", "昌平龙城花园", "石景山古城花园"};

        for (int i = 0; i < 8; i++) {
            Property p = new Property();
            p.setTitle(titles[i]);
            p.setPropertyNo("PRP" + String.format("%04d", i + 1));
            p.setType(types[i]);
            p.setArea((80 + i * 20) + "㎡");
            p.setAddress(addresses[i]);
            p.setCity(cities[i]);
            p.setDistrict(districts[i]);
            p.setPrice(new BigDecimal(prices[i]));
            p.setUnitPrice(new BigDecimal(prices[i] * 10000 / (80 + i * 20)));
            p.setRooms(rooms[i]);
            p.setHalls(halls[i]);
            p.setBathrooms(2);
            p.setFloor(String.valueOf(i % 10 + 1));
            p.setTotalFloor("28");
            p.setOrientation("南北通透");
            p.setDecoration("精装修");
            p.setBuildingType("板楼");
            p.setBuildingYear("201" + i);
            p.setDescription("核心地段，交通便利，配套完善");
            p.setFeatures("近地铁,学区房,精装修");
            p.setSupportingFacilities("学校,医院,超市,公园");
            p.setStatus(i < 6 ? "在售" : "已售");
            p.setImageUrl("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=modern%20luxury%20apartment%20building%20exterior&image_size=square");
            if (buildingList != null && i < buildingList.size()) {
                p.setBuildingDictId(buildingList.get(i).getId());
                p.setBuildingName(buildingList.get(i).getName());
            } else {
                p.setBuildingDictId(buildingIds[i]);
                p.setBuildingName(buildingNames[i]);
            }
            propertyRepository.save(p);
        }
    }

    private void initViewingRecords() {
        String[] satisfactions = {"非常满意", "满意", "一般", "不满意"};
        String[] agents = {"王经纪", "李经纪", "张经纪", "刘经纪"};
        String[] statuses = {"已完成", "待跟进", "已取消"};

        for (int i = 0; i < 10; i++) {
            ViewingRecord v = new ViewingRecord();
            v.setCustomerId((long) (i % 8 + 1));
            v.setCustomerName("客户" + (i % 8 + 1));
            v.setCustomerPhone("1380013800" + (i % 8 + 1));
            v.setPropertyId((long) (i % 8 + 1));
            v.setPropertyTitle("房源" + (i % 8 + 1));
            v.setPropertyAddress("房源地址" + (i % 8 + 1));
            v.setAgentName(agents[i % 4]);
            v.setViewingTime(LocalDateTime.now().plusDays(-i));
            v.setViewingDuration(60 + i * 10);
            v.setSatisfaction(satisfactions[i % 4]);
            v.setCustomerFeedback("客户反馈信息" + (i + 1));
            v.setFollowUpPlan("继续跟进，推荐其他房源");
            v.setStatus(statuses[i % 3]);
            v.setRemark("带看备注" + (i + 1));
            viewingRecordRepository.save(v);
        }
    }

    private void initVRPanoramas() {
        String[] panoramaNames = {"客厅全景", "主卧全景", "次卧全景", "厨房全景", "卫生间全景", "阳台全景"};

        for (int i = 1; i <= 6; i++) {
            for (int j = 0; j < 3; j++) {
                VRPanorama vr = new VRPanorama();
                vr.setPropertyId((long) i);
                vr.setPropertyTitle("房源" + i);
                vr.setName(panoramaNames[j]);
                vr.setDescription("这是" + panoramaNames[j] + "的360度全景展示");
                vr.setThumbnailUrl("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=" + panoramaNames[j] + "%20modern%20interior&image_size=square");
                vr.setPanoramaUrl("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=360%20panorama%20" + panoramaNames[j] + "%20spherical&image_size=square_hd");
                vr.setViewCount(100 + i * 10 + j * 5);
                vr.setSortOrder(j);
                vr.setStatus("启用");
                vrPanoramaRepository.save(vr);
            }
        }
    }

    private void initSubscriptions() {
        String[] statuses = {"待签约", "已签约", "已取消", "已完成"};
        String[] paymentMethods = {"一次性付款", "商业贷款", "公积金贷款", "组合贷款"};
        String[] agents = {"王经纪", "李经纪", "张经纪", "刘经纪"};

        for (int i = 0; i < 5; i++) {
            Subscription s = new Subscription();
            s.setSubscriptionNo("SUB2024" + String.format("%04d", i + 1));
            s.setCustomerId((long) (i + 1));
            s.setCustomerName("客户" + (i + 1));
            s.setCustomerPhone("1380013800" + (i + 1));
            s.setCustomerIdCard("11010119900101" + (1000 + i));
            s.setPropertyId((long) (i + 1));
            s.setPropertyTitle("房源" + (i + 1));
            s.setPropertyAddress("房源地址" + (i + 1));
            s.setTotalPrice(new BigDecimal(300 + i * 100));
            s.setUnitPrice(new BigDecimal(30000 + i * 2000));
            s.setArea(new BigDecimal(100 + i * 10));
            s.setDepositAmount(new BigDecimal(10 + i * 2));
            s.setFirstPayment(new BigDecimal(90 + i * 30));
            s.setRemainingAmount(new BigDecimal(210 + i * 70));
            s.setPaymentMethod(paymentMethods[i % 4]);
            s.setPaymentPeriod(30);
            s.setSignDate(LocalDateTime.now().plusDays(-i * 5));
            s.setDeliveryDate(LocalDateTime.now().plusMonths(12));
            s.setStatus(statuses[i % 4]);
            s.setAgentName(agents[i % 4]);
            s.setRemark("认购备注信息" + (i + 1));
            subscriptionRepository.save(s);
        }
    }

    private void initFollowUpRecords() {
        String[] followTypes = {"电话跟进", "微信跟进", "上门拜访", "短信提醒"};
        String[] followResults = {"意向加强", "保持联系", "需再考虑", "暂无购房计划"};
        String[] agents = {"王经纪", "李经纪", "张经纪", "刘经纪"};

        for (int i = 0; i < 15; i++) {
            FollowUpRecord f = new FollowUpRecord();
            f.setCustomerId((long) (i % 8 + 1));
            f.setCustomerName("客户" + (i % 8 + 1));
            f.setFollowType(followTypes[i % 4]);
            f.setFollowContent("跟进内容：了解客户最新购房需求，推荐合适房源");
            f.setFollowResult(followResults[i % 4]);
            f.setFollowTime(LocalDateTime.now().plusDays(-i));
            f.setNextFollowPlan("三天后再次电话联系，了解客户考虑情况");
            f.setNextFollowTime(LocalDateTime.now().plusDays(3));
            f.setAgentName(agents[i % 4]);
            f.setStatus("已完成");
            f.setRemark("跟进备注" + (i + 1));
            followUpRecordRepository.save(f);
        }
    }

    private void initAgents() {
        String[] names = {"王经纪", "李经纪", "张经纪", "刘经纪", "陈经纪", "赵经纪"};
        String[] phones = {"13900139001", "13900139002", "13900139003",
                          "13900139004", "13900139005", "13900139006"};
        String[] positions = {"资深经纪人", "高级经纪人", "经纪人",
                              "经纪人", "高级经纪人", "资深经纪人"};
        String[] departments = {"销售一部", "销售一部", "销售一部",
                                "销售二部", "销售二部", "销售二部"};

        for (int i = 0; i < 6; i++) {
            Agent a = new Agent();
            a.setName(names[i]);
            a.setPhone(phones[i]);
            a.setGender(i % 2 == 0 ? "男" : "女");
            a.setIdCard("110101198" + i + "0101" + (2000 + i));
            a.setPosition(positions[i]);
            a.setDepartment(departments[i]);
            a.setHireDate(LocalDateTime.now().plusMonths(-12 - i * 3));
            a.setStatus("在职");
            a.setRemark("经纪人备注信息" + (i + 1));
            agentRepository.save(a);
        }
    }

    private void initMonthlyTargets() {
        String currentMonth = LocalDateTime.now().getYear() + "-" +
                String.format("%02d", LocalDateTime.now().getMonthValue());
        String lastMonth = LocalDateTime.now().plusMonths(-1).getYear() + "-" +
                String.format("%02d", LocalDateTime.now().plusMonths(-1).getMonthValue());

        List<Agent> agents = agentRepository.findAll();
        int[] viewingTargets = {25, 20, 18, 22, 24, 28};
        int[] dealTargets = {5, 4, 3, 4, 5, 6};
        int[] amountTargets = {500, 400, 300, 400, 480, 600};
        double[] rateTargets = {25.0, 22.0, 20.0, 22.0, 24.0, 26.0};

        for (int i = 0; i < agents.size(); i++) {
            Agent agent = agents.get(i);
            MonthlyTarget t = new MonthlyTarget();
            t.setAgentId(agent.getId());
            t.setAgentName(agent.getName());
            t.setMonth(currentMonth);
            t.setViewingTarget(viewingTargets[i]);
            t.setDealTarget(dealTargets[i]);
            t.setDealAmountTarget(new BigDecimal(amountTargets[i]));
            t.setConversionRateTarget(new BigDecimal(rateTargets[i]));
            monthlyTargetRepository.save(t);
        }
    }

    private List<BuildingDict> initBuildings() {
        String[] names = {"朝阳公园花园", "国贸中心公寓", "望京新城", "海淀学府花园",
                         "通州新华家园", "亦庄林语墅", "昌平龙城花园", "石景山古城花园"};
        String[] cities = {"北京", "北京", "北京", "北京", "北京", "北京", "北京", "北京"};
        String[] districts = {"朝阳区", "朝阳区", "朝阳区", "海淀区", "通州区", "大兴区", "昌平区", "石景山区"};
        String[] addresses = {"朝阳区朝阳公园南路1号", "朝阳区建国门外大街1号", "朝阳区望京街9号",
                             "海淀区中关村大街1号", "通州区新华大街1号", "大兴区亦庄荣华路1号",
                             "昌平区回龙观别墅区1号", "石景山区古城大街1号"};
        String[] buildingTypes = {"板楼", "塔楼", "板塔结合", "板楼", "板楼", "板楼", "别墅", "板楼"};
        String[] buildingYears = {"2010", "2015", "2012", "2008", "2018", "2016", "2005", "2013"};
        String[] developers = {"万科地产", "万达集团", "保利地产", "中海地产",
                              "华润置地", "龙湖地产", "融创中国", "绿地集团"};
        String[] propertyCompanies = {"万科物业", "万达物业", "保利物业", "中海物业",
                                     "华润物业", "龙湖物业", "融创物业", "绿地物业"};
        String[] totalHouses = {"1200", "800", "1500", "900", "2000", "600", "300", "1000"};
        String[] plotRatios = {"2.5", "3.2", "2.8", "2.2", "3.0", "1.8", "0.8", "2.6"};
        String[] greeningRates = {"35%", "30%", "40%", "38%", "32%", "45%", "50%", "33%"};
        String[] parkingSpaces = {"1:1.2", "1:1", "1:1.5", "1:1.1", "1:1.3", "1:2", "1:2", "1:1"};
        String[][] facilities = {
            {"学校", "医院", "超市", "公园", "地铁"},
            {"商场", "写字楼", "酒店", "地铁", "超市"},
            {"学校", "地铁", "商场", "公园", "医院"},
            {"学校", "医院", "图书馆", "地铁", "公园"},
            {"地铁", "超市", "学校", "医院", "公园"},
            {"学校", "医院", "商场", "公园", "高尔夫"},
            {"会所", "泳池", "网球场", "公园", "学校"},
            {"地铁", "超市", "学校", "医院", "公园"}
        };
        String[] averagePrices = {"68000", "120000", "58000", "85000", "42000", "52000", "68000", "38000"};

        List<BuildingDict> buildingList = new java.util.ArrayList<>();
        for (int i = 0; i < 8; i++) {
            BuildingDict b = new BuildingDict();
            b.setName(names[i]);
            b.setCity(cities[i]);
            b.setDistrict(districts[i]);
            b.setAddress(addresses[i]);
            b.setBuildingType(buildingTypes[i]);
            b.setBuildingYear(buildingYears[i]);
            b.setDeveloper(developers[i]);
            b.setPropertyCompany(propertyCompanies[i]);
            b.setTotalHouses(totalHouses[i]);
            b.setPlotRatio(plotRatios[i]);
            b.setGreeningRate(greeningRates[i]);
            b.setParkingSpace(parkingSpaces[i]);
            b.setSupportingFacilities(String.join(",", facilities[i]));
            b.setDescription(names[i] + "位于" + districts[i] + "核心地段，交通便利，配套完善，是理想的居住选择。");
            b.setAveragePrice(averagePrices[i]);
            b.setImageUrl("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=modern%20luxury%20residential%20building%20complex&image_size=square_hd");
            buildingDictRepository.save(b);
            buildingList.add(b);
        }
        return buildingList;
    }
}
