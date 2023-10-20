package top.meethigher.ftp.client.pool.config;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.nio.charset.StandardCharsets;

/**
 * FTPPool配置
 * 参考自https://github.com/XiaZengming/FtpClientPool
 *
 * @author chenchuancheng
 * @since 2023/10/21 03:56
 */
public class FTPPoolConfig extends GenericObjectPoolConfig<FTPClient> {

    /**
     * 主机名
     */
    private String host;

    /**
     * 端口
     */
    private int port = 21;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 控制连接编码
     */
    private String controlEncoding = StandardCharsets.UTF_8.name();

    /**
     * 缓冲区大小, 非整数表示使用默认值
     */
    private int bufferSize = -1;

    /**
     * 传输数据格式
     */
    private int fileType = FTP.BINARY_FILE_TYPE;

    /**
     * 设置是否将 EPSV 与 IPv4 一起使用。
     */
    private boolean useEPSVWithIPv4 = false;

    /**
     * 是否启用被动模式
     */
    private boolean passiveMode = true;

    /**
     * 连接池空闲检测周期 毫秒
     */
    private long poolEvictIntervalMills = 30 * 1000;

    /**
     * 连接超时时间 毫秒
     */
    private int connectTimeoutMills = 10 * 1000;

    /**
     * 读取数据超时 毫秒 负数表示永不超时
     */
    private long dataTimeoutMills = -1;


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
}