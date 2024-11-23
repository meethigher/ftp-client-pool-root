package top.meethigher.ftp.client.pool.utils;

import org.apache.commons.net.ProtocolCommandEvent;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.SocketClient;
import org.slf4j.Logger;

/**
 * 接入Slf4j的日志模块
 *
 * @author <a href="https://meethigher.top">chenchuancheng</a>
 * @since 2023/10/22 21:34
 */
public class Slf4jPrintCommandListener implements ProtocolCommandListener {
    private final Logger log;
    private final boolean nologin;
    private final char eolMarker;
    private final boolean directionMarker;


    /**
     * Create the default instance which prints everything.
     *
     * @param log where to write the commands and responses
     */
    public Slf4jPrintCommandListener(final Logger log) {
        this(log, true, (char) 0);
    }


    /**
     * Create an instance which optionally suppresses login command text and indicates where the EOL starts with the specified character.
     *
     * @param log           where to write the commands and responses
     * @param suppressLogin if {@code true}, only print command name for login
     * @param eolMarker     if non-zero, add a marker just before the EOL.
     * @since 3.0
     */
    public Slf4jPrintCommandListener(final Logger log, final boolean suppressLogin, final char eolMarker) {
        this(log, suppressLogin, eolMarker, false);
    }


    /**
     * Create an instance which optionally suppresses login command text and indicates where the EOL starts with the specified character.
     *
     * @param log           where to write the commands and responses
     * @param suppressLogin if {@code true}, only print command name for login
     * @param eolMarker     if non-zero, add a marker just before the EOL.
     * @param showDirection if {@code true}, add {@code ">} " or {@code "< "} as appropriate to the output
     * @since 3.0
     */
    public Slf4jPrintCommandListener(final Logger log, final boolean suppressLogin, final char eolMarker, final boolean showDirection) {
        this.log = log;
        this.nologin = suppressLogin;
        this.eolMarker = eolMarker;
        this.directionMarker = showDirection;
    }

    private String getPrintableString(final String msg) {
        if (eolMarker == 0) {
            return msg;
        }
        final int pos = msg.indexOf(SocketClient.NETASCII_EOL);
        if (pos > 0) {
            return msg.substring(0, pos) +
                    eolMarker +
                    msg.substring(pos);
        }
        return msg;
    }

    @Override
    public void protocolCommandSent(final ProtocolCommandEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append(event.getSource().toString()).append(" sent: ");
        if (directionMarker) {
            sb.append("> ");
        }
        if (nologin) {
            final String cmd = event.getCommand();
            if ("PASS".equalsIgnoreCase(cmd) || "USER".equalsIgnoreCase(cmd)) {
                sb.append(cmd);
                sb.append(" *******"); // Don't bother with EOL marker for this!
            } else {
                final String IMAP_LOGIN = "LOGIN";
                if (IMAP_LOGIN.equalsIgnoreCase(cmd)) { // IMAP
                    String msg = event.getMessage();
                    msg = msg.substring(0, msg.indexOf(IMAP_LOGIN) + IMAP_LOGIN.length());
                    sb.append(msg);
                    sb.append(" *******"); // Don't bother with EOL marker for this!
                } else {
                    sb.append(getPrintableString(event.getMessage()));
                }
            }
        } else {
            sb.append(getPrintableString(event.getMessage()));
        }
        log.debug(sb.toString());
    }

    @Override
    public void protocolReplyReceived(final ProtocolCommandEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append(event.getSource().toString()).append(" received: ");
        if (directionMarker) {
            sb.append("< ");
        }
        sb.append(event.getMessage());
        log.debug(sb.toString());
    }
}
