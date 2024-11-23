package top.meethigher.ftp.client.pool.config;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;


/**
 * FTPPool配置
 *
 * @author <a href="https://meethigher.top">chenchuancheng</a>
 * @see <a href="https://github.com/XiaZengming/FtpClientPool">XiaZengming/FtpClientPool</a>
 * @since 2023/10/21 03:56
 */
public class FTPPoolConfig extends GenericObjectPoolConfig<FTPClient> {

    private static final char[] ID_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    private final Logger log = LoggerFactory.getLogger(FTPPoolConfig.class);

    /**
     * 主机名
     */
    private String host;

    /**
     * 端口
     */
    private int port;

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
    private String controlEncoding;

    /**
     * 缓冲区大小, 非整数表示使用默认值
     */
    private int bufferSize;

    /**
     * 传输数据格式
     */
    private int fileType;

    /**
     * 设置是否将 EPSV 与 IPv4 一起使用。
     * EPSV为扩展被动模式。
     * <p>
     * 默认行为
     * 在 IPv6 环境下，FTPClient 始终使用 EPSV 模式。
     * 在 IPv4 环境下，FTPClient 默认使用 PASV 模式。
     * <p>
     * 当调用 setUseEPSVwithIPv4(true) 时，FTPClient 会在 IPv4 环境下尝试使用 EPSV 模式进行数据连接。
     * 如果服务器不支持 EPSV 模式，则会回退到 PASV 模式。
     */
    private boolean useEPSVWithIPv4;

    /**
     * 是否启用被动模式
     */
    private boolean passiveMode;

    /**
     * 连接池空闲检测周期 毫秒
     */
    private long poolEvictIntervalMills;

    /**
     * 连接超时时间 毫秒
     */
    private int connectTimeoutMills;

    /**
     * 读取数据超时 毫秒 负数表示永不超时
     */
    private long dataTimeoutMills;

    /**
     * Slf4j日志调试
     */
    private boolean debug;


    private String poolName;

    public FTPPoolConfig() {
        this.host = "127.0.0.1";
        this.port = 21;
        this.username = "anonymous";
        this.password = "anonymous";
        this.controlEncoding = StandardCharsets.UTF_8.name();
        this.bufferSize = -1;
        this.fileType = FTP.BINARY_FILE_TYPE;
        this.useEPSVWithIPv4 = false;
        this.passiveMode = true;
        this.poolEvictIntervalMills = 30 * 1000;
        this.connectTimeoutMills = 10 * 1000;
        this.dataTimeoutMills = -1;
        this.debug = true;
        this.poolName = generatePoolName();
        /**
         * JMX 是一种 Java 技术， 允许开发者在运行时进行监控。开启后使用JConsole连接进程即可查看。
         * 注意：在springboot中，若出现MXBean already registered，启动类添加@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
         */
        this.setJmxEnabled(false);
    }

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

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }


    private String generatePoolName() {
        final String prefix = "FTPClientPool-";
        try {
            // Pool number is global to the VM to avoid overlapping pool numbers in classloader scoped environments
            synchronized (System.getProperties()) {
                final String next = String.valueOf(Integer.getInteger("top.meethigher.ftp.client.pool.config.pool_number", 0) + 1);
                System.setProperty("top.meethigher.ftp.client.pool.config.pool_number", next);
                return prefix + next;
            }
        } catch (Exception e) {
            // The SecurityManager didn't allow us to read/write system properties
            // so just generate a random pool number instead
            final ThreadLocalRandom random = ThreadLocalRandom.current();
            final StringBuilder buf = new StringBuilder(prefix);

            for (int i = 0; i < 4; i++) {
                buf.append(ID_CHARACTERS[random.nextInt(62)]);
            }
            log.info("assigned random pool name '{}' (security manager prevented access to system properties)", buf);
            return buf.toString();
        }
    }
}