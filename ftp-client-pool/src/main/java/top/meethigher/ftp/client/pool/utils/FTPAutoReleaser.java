package top.meethigher.ftp.client.pool.utils;

import org.apache.commons.net.ftp.FTPClient;
import top.meethigher.ftp.client.pool.FTPClientPool;

import java.util.Optional;

/**
 * FTP自动归还连接
 *
 * @author chenchuancheng
 * @since 2023/10/20 10:56
 */
public class FTPAutoReleaser {

    private final FTPClientPool pool;

    public FTPClientPool getPool() {
        return pool;
    }

    public FTPAutoReleaser(FTPClientPool pool) {
        this.pool = pool;
    }

    public <T> Optional<T> execute(FTPHandler<T> handler) throws Exception {
        FTPClient ftpClient = null;
        try {
            ftpClient = pool.borrowObject();
            return handler.handle(ftpClient);
        } finally {
            if (ftpClient != null) {
                pool.returnObject(ftpClient);
            }
        }
    }
}
