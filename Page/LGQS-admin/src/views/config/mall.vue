<template>
  <div class="app-container">
    <el-form ref="dataForm" :rules="rules" :model="dataForm" status-icon label-width="300px">
      <el-form-item label="餐厅名称" prop="lgqs_mall_name">
        <el-input v-model="dataForm.lgqs_mall_name"/>
      </el-form-item>
      <el-form-item label="餐厅地址" prop="lgqs_mall_address">
        <el-input v-model="dataForm.lgqs_mall_address"/>
      </el-form-item>
      <el-form-item label="地理坐标">
        <el-col :span="11">
          <el-input v-model="dataForm.lgqs_mall_longitude" placeholder="经度"/>
        </el-col>
        <el-col :span="2" style="text-align: center;">-</el-col>
        <el-col :span="11">
          <el-input v-model="dataForm.lgqs_mall_latitude" placeholder="纬度"/>
        </el-col>
      </el-form-item>
      <el-form-item label="联系电话" prop="lgqs_mall_phone">
        <el-input v-model="dataForm.lgqs_mall_phone"/>
      </el-form-item>
      <el-form-item label="联系QQ" prop="lgqs_mall_qq">
        <el-input v-model="dataForm.lgqs_mall_qq"/>
      </el-form-item>
      <el-form-item>
        <el-button @click="cancel">取消</el-button>
        <el-button type="primary" @click="update">确定</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { listMall, updateMall } from '@/api/config'

export default {
  name: 'ConfigMail',
  data() {
    return {
      dataForm: {
        lgqs_mall_name: '',
        lgqs_mall_address: '',
        lgqs_mall_phone: '',
        lgqs_mall_qq: '',
        lgqs_mall_longitude: '',
        lgqs_mall_latitude: ''
      },
      rules: {
        lgqs_mall_name: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ],
        lgqs_mall_address: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ],
        lgqs_mall_phone: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ],
        lgqs_mall_qq: [
          { required: true, message: '不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.init()
  },
  methods: {
    init: function() {
      listMall().then(response => {
        this.dataForm = response.data.data
      })
    },
    cancel() {
      this.init()
    },
    update() {
      this.$refs['dataForm'].validate((valid) => {
        if (!valid) {
          return false
        }
        this.doUpdate()
      })
    },
    doUpdate() {
      updateMall(this.dataForm)
        .then(response => {
          this.$notify.success({
            title: '成功',
            message: '餐厅配置成功'
          })
        })
        .catch(response => {
          this.$notify.error({
            title: '失败',
            message: response.data.errmsg
          })
        })
    }
  }
}
</script>
