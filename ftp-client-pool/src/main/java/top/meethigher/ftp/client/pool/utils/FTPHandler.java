package top.meethigher.ftp.client.pool.utils;

import org.apache.commons.net.ftp.FTPClient;

import java.util.Optional;


public interface FTPHandler<T> {

    Optional<T> handle(FTPClient ftpClient) throws Exception;
}
