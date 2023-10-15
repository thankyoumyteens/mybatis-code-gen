<template>
  <div id="app">
    <div style="height:40px;">
      <div style="display: inline-block;height:40px;line-height:40px;float: left">MyBatis Code Generator</div>
      <el-button type="primary" style="float: right" @click="handleAdd()">新建</el-button>
    </div>
    <el-table :data="dbList" style="width: 100%;" :header-cell-style="{'text-align':'center'}">
      <el-table-column prop="usersUrl" label="url"/>
      <el-table-column prop="usersPort" label="端口"/>
      <el-table-column prop="usersUid" label="用户名"/>
      <el-table-column prop="usersDb" label="数据库"/>
      <el-table-column prop="usersDriver" label="jdbc驱动"/>
      <el-table-column width="220" label="操作">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleGen(scope.row)">
            生成
          </el-button>
          <el-button v-if="false" size="mini" @click="handleEdit(scope.row)">
            编辑
          </el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="" :visible.sync="dialogVisibleAdd" width="40%">
      <el-form :model="form">
        <el-form-item label="url" :label-width="formLabelWidth">
          <el-input v-model="form.usersUrl"></el-input>
        </el-form-item>
        <el-form-item label="端口" :label-width="formLabelWidth">
          <el-input v-model="form.usersPort"></el-input>
        </el-form-item>
        <el-form-item label="用户名" :label-width="formLabelWidth">
          <el-input v-model="form.usersUid"></el-input>
        </el-form-item>
        <el-form-item label="密码" :label-width="formLabelWidth">
          <el-input v-model="form.usersPwd"></el-input>
        </el-form-item>
        <el-form-item label="数据库" :label-width="formLabelWidth">
          <el-input v-model="form.usersDb"></el-input>
        </el-form-item>
        <el-form-item label="数据库类型" :label-width="formLabelWidth">
          <el-select v-model="form.dbType" placeholder="请选择数据库类型">
            <el-option label="oracle" value="1"></el-option>
            <el-option label="mysql" value="2"></el-option>
            <el-option label="mariadb" value="3"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleAdd = false">取 消</el-button>
        <el-button type="primary" @click="handleModify()">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="" :visible.sync="dialogVisibleGen" width="40%">
      <el-form :model="formGen">
        <el-form-item label="url" :label-width="formLabelWidth">
          <el-input v-model="formGen.id" disabled></el-input>
        </el-form-item>
        <el-form-item label="表名" :label-width="formLabelWidth">
          <el-input v-model="formGen.tableName"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleGen = false">取 消</el-button>
        <el-button type="primary" @click="doGen()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      reqUrl: 'http://localhost:8081',
      dbList: [],
      isAdd: false,
      dialogVisibleAdd: false,
      dialogVisibleGen: false,
      form: {
        usersUrl: '',
        usersPort: '',
        usersUid: '',
        usersPwd: '',
        usersDb: '',
        dbType: '',
      },
      formGen: {
        id: '',
        tableName: '',
        useLombok: true,
        useComment: true,
      },
      formLabelWidth: '120px',
    }
  },
  mounted() {
    this.getTableList();
  },
  methods: {
    getTableList() {
      axios.get(`${this.reqUrl}/main/list`).then((res) => {
        this.dbList = res['data']['data']
      });
    },
    handleAdd() {
      this.isAdd = true;
      this.dialogVisibleAdd = true;
    },
    handleGen(row) {
      this.formGen.id  = row['usersId'];
      this.dialogVisibleGen = true;
    },
    handleModify() {
      axios.post(`${this.reqUrl}/main/add`, {
        ...this.form
      }).then((res) => {
        if (res['data']['code'] === 200) {
          this.$message({
            type: 'success',
            message: '操作成功!'
          });
          this.dialogVisibleAdd = false;
          this.getTableList();
        } else {
          this.$message({
            type: 'error',
            message: '操作失败!'
          });
        }
      })
    },
    doGen() {
      axios.post(`${this.reqUrl}/main/gen`, {
        ...this.formGen
      }).then((res) => {
        if (res['data']['code'] === 200) {
          this.$message({
            type: 'success',
            message: '操作成功!'
          });
          this.dialogVisibleGen = false;
          this.getTableList();
        } else {
          this.$message({
            type: 'error',
            message: '操作失败!'
          });
        }
      }).catch((res) => {
        this.$message({
          type: 'error',
          message: '操作失败!'
        });
      })
    },
    handleDelete(row) {
      this.$confirm('确认删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        axios.post(`${this.reqUrl}/main/del`, {
          usersId: row['usersId']
        }).then((res) => {
          console.log(res['data']['code'])
          if (res['data']['code'] === 200) {
            this.$message({
              type: 'success',
              message: '删除成功!'
            });
            this.getTableList();
          } else {
            this.$message({
              type: 'error',
              message: '删除失败!'
            });
          }
        })
      });
    }
  }
}
</script>

<style>
#app {
  font-family: Helvetica, sans-serif;
  text-align: center;
}
</style>
