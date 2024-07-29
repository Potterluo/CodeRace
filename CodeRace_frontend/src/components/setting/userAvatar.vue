<template>
  <div class="auth" v-if="loginUser.userName === '未登录'">
    <a-button type="primary" @click="controlLogin">登录</a-button>
    <AuthModal />
  </div>
  <div v-else>
    <a-dropdown trigger="hover">
      <a-row>
        <a-col span="8">
          <template v-if="loginUser.userAvatar">
            <img class="avatar-image" alt="avatar" :src="loginUser.userAvatar" />
          </template>
          <template v-else>
            <a-avatar :style="{ backgroundColor: '#3370ff' }">
              <icon-user />
            </a-avatar>
          </template>
        </a-col>
        <a-col  span="16" class="user-name" >
          <p v-if="theme === 'light'" style="color: #1e1e1e">{{ loginUser.userName }}</p>
          <p v-else style="color: #fff">{{ loginUser.userName }}</p>
        </a-col>
      </a-row>
      <template #content>
        <a-doption @click="handleAbout">
          <icon-home :style="{ marginRight: '6px' }" />个人中心
        </a-doption>
        <a-doption @click="logout">
          <icon-export :style="{ marginRight: '6px' }" />退出登录
        </a-doption>
      </template>
    </a-dropdown>
  </div>
</template>

<script setup lang="ts">
import { useStore } from "vuex";
import { computed } from "vue";
import { IconUser, IconHome, IconExport } from "@arco-design/web-vue/es/icon";
import { LoginUserVO, UserControllerService } from "../../../generated";
import AuthModal from "@/components/Modals/AuthModal.vue";
import { useRouter } from "vue-router";
const theme = computed(() => store.state.theme.theme);
const router = useRouter();

const store = useStore();

// 获取相关信息
const loginUser = computed<LoginUserVO>(() => store.state.user.loginUser);

// 退出登录
const logout = () => {
  UserControllerService.userLogoutUsingPost();
  location.reload();
};
const handleAbout = () => {
  router.push({
    path: "/about",
    replace: true,
  });
};

// 控制登录弹窗
function controlLogin() {
  store.commit("auth/updateModal", { modelName: "login", value: true });
}
</script>

<style scoped>
.auth {
  display: flex;
  gap: 5px;
  margin: 15px;
}
.avatar-image {
  width: 2rem;
  height: 2rem;
  border-radius: 50%;
  object-fit: cover;
}
.user-name {
  text-align: center;
  align-items: center;
  display: flex;
  height: 2rem;
}
:deep(.arco-avatar) {
  width: 2rem;
  height: 2rem;
  border-radius: 50%;
  object-fit: cover;
}
</style>
