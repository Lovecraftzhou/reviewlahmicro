# node镜像
FROM node:18.18.2 as build-stage
RUN echo "-------------------- web环境配置 --------------------"

# 指定接下来的工作路径为/reviewlah-vue  - 类似于cd命令
WORKDIR /reviewlah-vue
# 拷贝前端项目到目录下
COPY . .
# 安装依赖
RUN npm install

RUN npm run build
# 前端项目运行命令
#CMD ["npm","run","serve"]


# ======================== 上：npm打包  下：nginx运行 ========================
# nginx镜像
FROM nginx as production-stage

# 移除nginx容器的default.conf文件、nginx配置文件
RUN rm /etc/nginx/conf.d/default.conf
# 把主机的nginx.conf文件复制到nginx容器的/etc/nginx文件夹下
COPY --from=build-stage /reviewlah-vue/nginx.conf /etc/nginx/conf.d/default.conf
# 拷贝前端vue项目打包后生成的文件到nginx下运行
COPY --from=build-stage /reviewlah-vue/dist /usr/share/nginx/html

# 暴露8101端口
EXPOSE 8182

# 注：CMD不同于RUN，CMD用于指定在容器启动时所要执行的命令，而RUN用于指定镜像构建时所要执行的命令。
#    RUN指令创建的中间镜像会被缓存，并会在下次构建中使用。如果不想使用这些缓存镜像，可以在构建时指定--no-cache参数，如：docker build --no-cache
# 使用daemon off的方式将nginx运行在前台保证镜像不至于退出
CMD ["nginx", "-g", "daemon off;"]
