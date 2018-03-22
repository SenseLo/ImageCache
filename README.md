# ImageCache
图片的三级缓存实现https://www.cnblogs.com/itgungnir/p/6211002.html

三级缓存的流程：APP加载图片时，先在LruCache找图片，如果有直接取出，如果没有去SoftReference寻找，如果有取出并且存放到LruCache中，
              如果没有去文件系统中寻找，如果有则取出并添加到LruCache中，如果没有则连接网络从网上下载图片，下载完成后将图片保存到
              文件系统中然后放到LruCache中。

