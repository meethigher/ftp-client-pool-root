package top.meethigher.ftp.client.pool.starter;

import java.nio.charset.StandardCharsets;

public class FTPPoolConfigProperties {

    /**
     * 主机地址
     */
    private String host;

    /**
     * 端口
     */
    private int port = 21;

    /**
     * 用户名
     */
    private String username = "anonymous";

    /**
     * 密码
     */
    private String password = "anonymous";

    /**
     * 控制连接编码
     */
    private String controlEncoding = StandardCharsets.UTF_8.name();

    /**
     * 缓冲区大小，非整数表示使用默认值
     */
    private int bufferSize = -1;

    /**
     * 数据传输格式
     * 示例org.apache.commons.net.ftp.FTP#BINARY_FILE_TYPE
     */
    private int fileType = 2;

    /**
     * IPv4环境下，使用EPSV替代PASV进入被动模式
     */
    private boolean useEPSVWithIPv4 = false;

    /**
     * 是否启用被动模式
     */
    private boolean passiveMode = true;

    /**
     * 连接池空闲监测周期，单位毫秒
     */
    private long poolEvictIntervalMills = 30 * 1000;

    /**
     * 连接超时时间，单位毫秒
     */
    private int connectTimeoutMills = 10 * 1000;

    /**
     * 读取数据超时时间，单位毫秒。负数表示永不超时
     */
    private long dataTimeoutMills = -1;

    /**
     * 开启debug日志输出
     */
    private boolean debug = true;

    /**
     * 是否启用jmx
     */
    private boolean jmxEnabled = false;

    /**
     * 池最小空闲连接
     */
    private int minIdle = 0;

    /**
     * 池最大空闲连接
     */
    private int maxIdle = 8;

    /**
     * 池最大连接
     */
    private int maxTotal = 8;

    /**
     * 借用时进行测试
     */
    private boolean testOnBorrow = true;

    /**
     * 创建时进行测试
     */
    private boolean testOnCreate = true;

    /**
     * 归还时进行测试
     */
    private boolean testOnReturn = true;

    /**
     * 空闲时进行测试
     */
    private boolean testWhileIdle = true;

    /**
     * 池懒加载
     */
    private boolean lazy = true;


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getControlEncoding() {
        return controlEncoding;
    }

    public void setControlEncoding(String controlEncoding) {
        this.controlEncoding = controlEncoding;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public boolean isUseEPSVWithIPv4() {
        return useEPSVWithIPv4;
    }

    public void setUseEPSVWithIPv4(boolean useEPSVWithIPv4) {
        this.useEPSVWithIPv4 = useEPSVWithIPv4;
    }

    public boolean isPassiveMode() {
        return passiveMode;
    }

    public void setPassiveMode(boolean passiveMode) {
        this.passiveMode = passiveMode;
    }

    public long getPoolEvictIntervalMills() {
        return poolEvictIntervalMills;
    }

    public void setPoolEvictIntervalMills(long poolEvictIntervalMills) {
        this.poolEvictIntervalMills = poolEvictIntervalMills;
    }

    public int getConnectTimeoutMills() {
        return connectTimeoutMills;
    }

    public void setConnectTimeoutMills(int connectTimeoutMills) {
        this.connectTimeoutMills = connectTimeoutMills;
    }

    public long getDataTimeoutMills() {
        return dataTimeoutMills;
    }

    public void setDataTimeoutMills(long dataTimeoutMills) {
        this.dataTimeoutMills = dataTimeoutMills;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isJmxEnabled() {
        return jmxEnabled;
    }

    public void setJmxEnabled(boolean jmxEnabled) {
        this.jmxEnabled = jmxEnabled;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnCreate() {
        return testOnCreate;
    }

    public void setTestOnCreate(boolean testOnCreate) {
        this.testOnCreate = testOnCreate;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public boolean isLazy() {
        return lazy;
    }

    public void setLazy(boolean lazy) {
        this.lazy = lazy;
    }
}
