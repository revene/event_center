package com.blanc.event.service.register;

/**
 * 功能：调用服务的注册
 *
 * @author chuchengyi
 */
public interface ConsumerServiceRegister<T> {


    /**
     * 功能：注册一个服务
     *
     * @param app
     * @param serviceClass
     */
    public void registerService(String app, Class<T> serviceClass);


    /**
     * 功能：判断某个服务是否已经注册过了
     *
     * @param app
     * @param serviceClass
     * @return
     */
    public boolean hadRegisterService(String app, Class<T> serviceClass);


    /**
     * 功能：注册一个直接调用的服务
     *
     * @param app
     * @param ip
     * @param serviceClass
     */
    public void registerDirectService(String app, String ip, Class<T> serviceClass);


    /**
     * 功能：判断某个直接调用的服务是否已经注册了
     *
     * @param app
     * @param ip
     * @param serviceClass
     * @return
     */
    public boolean hadRegisterDirectService(String app, String ip, Class<T> serviceClass);
}
