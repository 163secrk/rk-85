<template>
  <div class="page-container">
    <div class="page-header">
      <div class="page-title">合同详情</div>
      <div>
        <el-button icon="el-icon-edit" type="primary" @click="goToEdit" :disabled="contract.status !== '草稿'">编辑</el-button>
        <el-button icon="el-icon-printer" type="success" @click="handlePrint">打印</el-button>
        <el-button icon="el-icon-download" type="warning" @click="handleExport">导出PDF</el-button>
        <el-button icon="el-icon-arrow-left" @click="goBack">返回</el-button>
      </div>
    </div>

    <div id="contract-print-area">
      <div class="contract-header">
        <div class="contract-title">房屋买卖合同</div>
        <div class="contract-no">合同编号：{{ contract.contractNo }}</div>
        <div class="contract-status">
          <el-tag :type="getStatusType(contract.status)" size="medium">{{ contract.status }}</el-tag>
        </div>
      </div>

      <div class="contract-content">
        <div class="contract-section">
          <div class="section-title">第一条 合同双方当事人</div>
          <el-row :gutter="40">
            <el-col :span="12">
              <div class="party-title">买方（以下简称乙方）：</div>
              <div class="party-info">
                <div class="info-row">
                  <span class="label">姓名：</span>
                  <span class="value">{{ contract.customerName }}</span>
                </div>
                <div class="info-row">
                  <span class="label">身份证号：</span>
                  <span class="value">{{ contract.customerIdCard }}</span>
                </div>
                <div class="info-row">
                  <span class="label">联系电话：</span>
                  <span class="value">{{ contract.customerPhone }}</span>
                </div>
                <div class="info-row">
                  <span class="label">联系地址：</span>
                  <span class="value">{{ contract.customerAddress || '-' }}</span>
                </div>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="party-title">卖方（以下简称甲方）：</div>
              <div class="party-info">
                <div class="info-row">
                  <span class="label">姓名：</span>
                  <span class="value">{{ contract.sellerName || '-' }}</span>
                </div>
                <div class="info-row">
                  <span class="label">身份证号：</span>
                  <span class="value">{{ contract.sellerIdCard || '-' }}</span>
                </div>
                <div class="info-row">
                  <span class="label">联系电话：</span>
                  <span class="value">{{ contract.sellerPhone || '-' }}</span>
                </div>
                <div class="info-row">
                  <span class="label">联系地址：</span>
                  <span class="value">{{ contract.sellerAddress || '-' }}</span>
                </div>
              </div>
            </el-col>
          </el-row>
        </div>

        <div class="contract-section">
          <div class="section-title">第二条 房屋基本情况</div>
          <div class="info-grid">
            <div class="info-row">
              <span class="label">房屋名称：</span>
              <span class="value">{{ contract.propertyTitle }}</span>
            </div>
            <div class="info-row">
              <span class="label">房屋坐落：</span>
              <span class="value">{{ contract.propertyAddress }}</span>
            </div>
            <div class="info-row">
              <span class="label">房屋编号：</span>
              <span class="value">{{ contract.propertyNo || '-' }}</span>
            </div>
            <div class="info-row">
              <span class="label">房屋类型：</span>
              <span class="value">{{ contract.propertyType || '-' }}</span>
            </div>
            <div class="info-row">
              <span class="label">建筑面积：</span>
              <span class="value">{{ contract.area }} 平方米</span>
            </div>
            <div class="info-row">
              <span class="label">房屋户型：</span>
              <span class="value">{{ contract.rooms }}室{{ contract.halls }}厅{{ contract.bathrooms }}卫</span>
            </div>
            <div class="info-row">
              <span class="label">所在楼层：</span>
              <span class="value">{{ contract.floor || '-' }}</span>
            </div>
            <div class="info-row">
              <span class="label">房屋朝向：</span>
              <span class="value">{{ contract.orientation || '-' }}</span>
            </div>
            <div class="info-row">
              <span class="label">装修情况：</span>
              <span class="value">{{ contract.decoration || '-' }}</span>
            </div>
          </div>
        </div>

        <div class="contract-section">
          <div class="section-title">第三条 成交价格与付款方式</div>
          <div class="price-section">
            <div class="total-price">
              <span class="label">房屋成交总价：</span>
              <span class="value">人民币 {{ contract.totalPrice }} 万元整（¥{{ formatMoney(contract.totalPrice * 10000) }}）</span>
            </div>
            <div class="info-grid">
              <div class="info-row">
                <span class="label">单价：</span>
                <span class="value">{{ contract.unitPrice }} 元/平方米</span>
              </div>
              <div class="info-row">
                <span class="label">定金：</span>
                <span class="value">人民币 {{ contract.depositAmount }} 万元整</span>
              </div>
              <div class="info-row">
                <span class="label">首付金额：</span>
                <span class="value">人民币 {{ contract.firstPayment || 0 }} 万元整</span>
              </div>
              <div class="info-row">
                <span class="label">剩余房款：</span>
                <span class="value">人民币 {{ contract.remainingAmount || 0 }} 万元整</span>
              </div>
              <div class="info-row">
                <span class="label">付款方式：</span>
                <span class="value">{{ contract.paymentMethod }}</span>
              </div>
              <div class="info-row">
                <span class="label">贷款年限：</span>
                <span class="value">{{ contract.paymentPeriod || 0 }} 年</span>
              </div>
            </div>
          </div>
        </div>

        <div class="contract-section">
          <div class="section-title">第四条 房屋交付</div>
          <div class="info-grid">
            <div class="info-row">
              <span class="label">签署日期：</span>
              <span class="value">{{ contract.signDate | formatDate }}</span>
            </div>
            <div class="info-row">
              <span class="label">交房日期：</span>
              <span class="value">{{ contract.deliveryDate | formatDate }}</span>
            </div>
            <div class="info-row" v-if="contract.recordDate">
              <span class="label">备案日期：</span>
              <span class="value">{{ contract.recordDate | formatDate }}</span>
            </div>
            <div class="info-row">
              <span class="label">经办人：</span>
              <span class="value">{{ contract.agentName || '-' }}</span>
            </div>
          </div>
        </div>

        <div class="contract-section" v-if="contract.remark">
          <div class="section-title">第五条 其他约定事项</div>
          <div class="remark-content">{{ contract.remark }}</div>
        </div>

        <div class="contract-signatures">
          <div class="signature-block">
            <div class="signature-title">甲方（卖方）签字：</div>
            <div class="signature-line"></div>
            <div class="signature-date">日期：</div>
          </div>
          <div class="signature-block">
            <div class="signature-title">乙方（买方）签字：</div>
            <div class="signature-line"></div>
            <div class="signature-date">日期：</div>
          </div>
        </div>
      </div>
    </div>

    <div class="status-timeline no-print">
      <div class="timeline-title">合同状态流转</div>
      <el-steps :active="getStatusStep(contract.status)" finish-status="success" process-status="primary">
        <el-step title="草稿" description="合同创建"></el-step>
        <el-step title="已签署" description="双方签字确认"></el-step>
        <el-step title="已备案" description="房管部门备案"></el-step>
        <el-step title="已完结" description="交易完成"></el-step>
      </el-steps>
      <div class="status-actions" v-if="contract.status !== '已完结'">
        <el-button v-if="contract.status === '草稿'" type="primary" @click="updateStatus('已签署')">
          <i class="el-icon-edit-outline"></i> 签署合同
        </el-button>
        <el-button v-if="contract.status === '已签署'" type="warning" @click="updateStatus('已备案')">
          <i class="el-icon-upload2"></i> 合同备案
        </el-button>
        <el-button v-if="contract.status === '已备案'" type="success" @click="updateStatus('已完结')">
          <i class="el-icon-circle-check"></i> 完结合同
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ContractDetail',
  data() {
    return {
      contract: {}
    }
  },
  mounted() {
    const id = this.$route.params.id
    this.loadDetail(id)
  },
  methods: {
    async loadDetail(id) {
      const res = await this.$axios.get(`/contracts/${id}`)
      if (res.code === 200) {
        this.contract = res.data
      }
    },
    formatMoney(num) {
      if (!num) return '0.00'
      return num.toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ',')
    },
    goToEdit() {
      this.$router.push(`/contracts/edit/${this.contract.id}`)
    },
    goBack() {
      this.$router.push('/contracts')
    },
    getStatusType(status) {
      const map = {
        '草稿': 'info',
        '已签署': 'primary',
        '已备案': 'warning',
        '已完结': 'success'
      }
      return map[status] || 'info'
    },
    getStatusStep(status) {
      const map = {
        '草稿': 0,
        '已签署': 1,
        '已备案': 2,
        '已完结': 3
      }
      return map[status] !== undefined ? map[status] + 1 : 0
    },
    async updateStatus(status) {
      this.$confirm(`确定要将合同状态更新为"${status}"吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const res = await this.$axios.put(`/contracts/${this.contract.id}/status?status=${status}`)
        if (res.code === 200) {
          this.$message.success('状态更新成功')
          this.loadDetail(this.contract.id)
        } else {
          this.$message.error(res.message)
        }
      }).catch(() => {})
    },
    handlePrint() {
      const printContent = document.getElementById('contract-print-area')
      const printWindow = window.open('', '_blank')
      printWindow.document.write(`
        <!DOCTYPE html>
        <html>
        <head>
          <title>房屋买卖合同 - ${this.contract.contractNo}</title>
          <style>
            * { margin: 0; padding: 0; box-sizing: border-box; }
            body { font-family: 'SimSun', '宋体', serif; padding: 30px; line-height: 1.8; color: #333; }
            .contract-header { text-align: center; margin-bottom: 30px; border-bottom: 2px solid #333; padding-bottom: 20px; }
            .contract-title { font-size: 28px; font-weight: bold; margin-bottom: 10px; }
            .contract-no { font-size: 14px; color: #666; }
            .contract-section { margin-bottom: 25px; }
            .section-title { font-size: 16px; font-weight: bold; margin-bottom: 15px; padding-left: 10px; border-left: 4px solid #333; }
            .party-title { font-weight: bold; margin-bottom: 10px; font-size: 15px; }
            .party-info { padding-left: 20px; }
            .info-grid { display: flex; flex-wrap: wrap; padding-left: 20px; }
            .info-row { width: 50%; margin-bottom: 8px; display: flex; }
            .info-row .label { color: #666; min-width: 100px; }
            .info-row .value { flex: 1; }
            .price-section { padding-left: 20px; }
            .total-price { font-size: 18px; font-weight: bold; color: #d9534f; margin-bottom: 15px; padding: 15px; background: #f9f9f9; border-left: 4px solid #d9534f; }
            .remark-content { padding: 15px; background: #f9f9f9; border-radius: 4px; margin-left: 20px; white-space: pre-wrap; }
            .contract-signatures { display: flex; justify-content: space-between; margin-top: 60px; padding-top: 40px; border-top: 1px dashed #999; }
            .signature-block { text-align: center; width: 45%; }
            .signature-title { margin-bottom: 60px; }
            .signature-line { border-bottom: 1px solid #333; margin-bottom: 10px; }
            @media print {
              body { padding: 20px; }
              .no-print { display: none !important; }
            }
          </style>
        </head>
        <body>
          ${printContent.innerHTML}
        </body>
        </html>
      `)
      printWindow.document.close()
      printWindow.focus()
      setTimeout(() => {
        printWindow.print()
      }, 500)
    },
    handleExport() {
      this.handlePrint()
    }
  }
}
</script>

<style scoped>
#contract-print-area {
  background: #fff;
  padding: 40px;
  margin-bottom: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.contract-header {
  text-align: center;
  margin-bottom: 30px;
  border-bottom: 2px solid #333;
  padding-bottom: 20px;
  position: relative;
}

.contract-title {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 10px;
  color: #333;
}

.contract-no {
  font-size: 14px;
  color: #666;
}

.contract-status {
  position: absolute;
  right: 0;
  top: 0;
}

.contract-section {
  margin-bottom: 25px;
}

.section-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 15px;
  padding-left: 10px;
  border-left: 4px solid #409EFF;
  color: #303133;
}

.party-title {
  font-weight: bold;
  margin-bottom: 10px;
  font-size: 15px;
  color: #303133;
}

.party-info {
  padding-left: 20px;
}

.info-grid {
  display: flex;
  flex-wrap: wrap;
  padding-left: 20px;
}

.info-row {
  width: 50%;
  margin-bottom: 8px;
  display: flex;
}

.info-row .label {
  color: #909399;
  min-width: 100px;
  flex-shrink: 0;
}

.info-row .value {
  flex: 1;
  color: #303133;
}

.price-section {
  padding-left: 20px;
}

.total-price {
  font-size: 18px;
  font-weight: bold;
  color: #f56c6c;
  margin-bottom: 15px;
  padding: 15px;
  background: #fef0f0;
  border-left: 4px solid #f56c6c;
  border-radius: 4px;
}

.remark-content {
  padding: 15px;
  background: #f5f7fa;
  border-radius: 4px;
  margin-left: 20px;
  white-space: pre-wrap;
  line-height: 1.8;
  color: #606266;
}

.contract-signatures {
  display: flex;
  justify-content: space-between;
  margin-top: 60px;
  padding-top: 40px;
  border-top: 1px dashed #dcdfe6;
}

.signature-block {
  text-align: center;
  width: 45%;
}

.signature-title {
  margin-bottom: 60px;
  font-weight: 500;
}

.signature-line {
  border-bottom: 1px solid #333;
  margin-bottom: 10px;
}

.signature-date {
  text-align: left;
  color: #666;
}

.status-timeline {
  background: #fff;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.timeline-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 20px;
  color: #303133;
}

.status-actions {
  margin-top: 20px;
  text-align: center;
}

@media print {
  .no-print {
    display: none !important;
  }
}
</style>
