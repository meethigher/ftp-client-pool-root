package top.meethigher.ftp.client.pool.utils;

import org.apache.commons.net.ftp.FTPClient;


public interface FTPHandler<T> {

    T handle(FTPClient ftpClient) throws Exception;
}
