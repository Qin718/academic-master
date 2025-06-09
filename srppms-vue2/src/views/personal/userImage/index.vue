<template>
  <div>
    <div class="user-info-head" @click="editCropper()">
      <img alt="点击上传头像" class="img-circle img-lg" height="120px" v-bind:src="userInfo.image" width="120px"/>
    </div>
    <el-dialog :title="title" :visible.sync="cropperWindow" append-to-body width="800px">
      <el-row>
        <el-col :md="12" :style="{height: '350px'}" :xs="24">
          <vue-cropper
            ref="cropper"
            :autoCrop="options.autoCrop"
            :outputSize="options.outputSize"
            :autoCropHeight="options.autoCropHeight"
            :autoCropWidth="options.autoCropWidth"
            :fixedBox="options.fixedBox"
            :img="options.image"
            :info="true"
            :outputType="options.outputType"
            @realTime="realTime"
          />
        </el-col>
        <el-col :md="12" :style="{height: '350px'}" :xs="24">
          <div class="avatar-upload-preview">
            <img :src="options.image" :style="previews.img" alt=""/>
          </div>
        </el-col>
      </el-row>
      <br/>
      <el-row>
        <el-col :lg="2" :sm="3" :xs="3">
          <el-upload :before-upload="beforeUpload" :http-request="requestUpload" :show-file-list="false" action="#">
            <el-button size="small">
              选择
              <i class="el-icon-upload el-icon--right"></i>
            </el-button>
          </el-upload>
        </el-col>
        <el-col :lg="{span: 1, offset: 2}" :sm="2" :xs="2">
          <el-button icon="el-icon-plus" size="small" @click="changeScale(1)"></el-button>
        </el-col>
        <el-col :lg="{span: 1, offset: 1}" :sm="2" :xs="2">
          <el-button icon="el-icon-minus" size="small" @click="changeScale(-1)"></el-button>
        </el-col>
        <el-col :lg="{span: 1, offset: 1}" :sm="2" :xs="2">
          <el-button icon="el-icon-refresh-left" size="small" @click="rotateLeft()"></el-button>
        </el-col>
        <el-col :lg="{span: 1, offset: 1}" :sm="2" :xs="2">
          <el-button icon="el-icon-refresh-right" size="small" @click="rotateRight()"></el-button>
        </el-col>
        <el-col :lg="{span: 2, offset: 6}" :sm="2" :xs="2">
          <el-button size="small" type="primary" @click="uploadImg()">提 交</el-button>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script>
import {VueCropper} from "vue-cropper";
import {mapGetters} from "vuex";
import {message} from "@/method/message";

export default {
  components: {VueCropper},
  data() {
    return {
      // 是否显示弹出层
      cropperWindow: false,
      // 弹出层标题
      title: "修改头像",
      options: {
        image: "",  //裁剪的图片
        autoCrop: true,             // 是否默认生成截图框
        outputSize: 1,              //裁剪生成图片的质量(可选0.1 - 1)
        autoCropWidth: 200,         // 默认生成截图框宽度
        autoCropHeight: 200,        // 默认生成截图框高度
        fixedBox: true,             // 固定截图框大小 不允许改变
        outputType: "JPG",          // 默认生成截图为PNG格式
        filename: 'image'           // 文件名称
      },
      previews: {},
    };
  },
  computed: {
    ...mapGetters(["userInfo"]),
  },
  methods: {
    // 编辑头像
    editCropper() {
      this.cropperWindow = true;
      this.options.image = this.userInfo.image
    },
    // 刷新组件
    refresh() {
      this.$refs.cropper.refresh();
    },
    // 覆盖默认的上传行为
    requestUpload() {
    },
    // 向左旋转
    rotateLeft() {
      this.$refs.cropper.rotateLeft();
    },
    // 向右旋转
    rotateRight() {
      this.$refs.cropper.rotateRight();
    },
    // 图片缩放
    changeScale(num) {
      num = num || 1;
      this.$refs.cropper.changeScale(num);
    },
    // 上传预处理
    beforeUpload(file) {
      if (file.type.indexOf("image/") === -1) {
        message.warning("文件格式错误，请上传图片类型,如：JPG，PNG后缀的文件。");
      } else {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => {
          this.options.image = reader.result
          this.options.filename = file.name;
        };
      }
    },
    // 上传图片
    uploadImg() {
	    this.$refs.cropper.getCropBlob(data => {
		    this.$UploadFile("/import/change/image", data)
			    .then(() => {
				    this.cropperWindow = false;
				    this.$GlobalLoading(true, {text: "正在刷新，请稍候..."});
	          setTimeout(()=>{
	            this.$GlobalLoading(false,null)
	            this.$router.push("/Index")
		          this.setUserInfo()
	          }, 1000)
			    });
	    })
    },
	  setUserInfo() {
		  this.$refs.cropper.getCropData(image => {
			  this.userInfo.image = image
			  this.$store.dispatch('userInfo/setUserInfo', this.userInfo.image)
		  })
	  },
    // 实时预览
    realTime(data) {
      this.previews = data;
    },
  }
};
</script>
<style lang="less" scoped>
/* image */
.img-circle {
  border-radius: 50%;
}

.img-lg {
  width: 120px;
  height: 120px;
}

.avatar-upload-preview {
  position: relative;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 200px;
  height: 200px;
  border-radius: 50%;
  box-shadow: 0 0 4px #ccc;
  overflow: hidden;
}

.user-info-head {
  position: relative;
  display: inline-block;
  height: 120px;
}

.user-info-head:hover:after {
  content: '+';
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  color: #eee;
  background: rgba(0, 0, 0, 0.5);
  font-size: 24px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  cursor: pointer;
  line-height: 110px;
  border-radius: 50%;
}
</style>
