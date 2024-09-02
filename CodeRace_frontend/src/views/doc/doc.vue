<script setup lang="ts">

</script>

<template>
  <div class="info center">
    <a-card hoverable :style="{ width: '70vw' }" class="aboutCard">
      <div class="title" style="font-size: 30px">项目说明</div>
      <div class="content">
        <div class="content-item">
          <div class="content-item-content" style="font-size: 24px">
            这是一个基于Vue3+Vite2+Arco-Design-Vue2.0的在线编程平台，
            主要功能有：在线做题、提交代码、查看代码、查看排名、查看题目、查看个人信息、修改个人信息、查看项目说明等。
            试用管理员账号：admin密码：12345678
          </div>
        </div>
        <div class="content-item">
        </div>
      </div>
    </a-card>
  </div>
</template>

<style scoped>
.center {
  display: flex;
  align-items: center;
  justify-content: center;
}
.content {
  width: 100%;
  flex-direction: column;
}
.arco-avatar {
  width: 80px;
  height: 80px;
  margin-bottom: 10px;
}
.arco-card-size-medium {
  font-size: 8px;
}
.save {
  align-self: flex-end;
}
:deep(.vch__container .vch__legend) {
  display: none;
}
</style>
