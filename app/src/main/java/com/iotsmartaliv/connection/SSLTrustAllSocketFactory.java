package com.iotsmartaliv.connection;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.IOException;
import java.net.Socket;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * This activity class is used for SSL connection.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class SSLTrustAllSocketFactory extends SSLSocketFactory {

    private static final String TAG = "SSLTrustAllSocketFactory";
    private SSLContext mCtx;

    public SSLTrustAllSocketFactory(KeyStore truststore) throws Throwable {
        super(truststore);
        try {
            mCtx = SSLContext.getInstance("TLS");
            mCtx.init(null, new TrustManager[]{new SSLTrustAllManager()},
                    null);
            setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        } catch (Exception ex) {
        }
    }

    public static SSLSocketFactory getSocketFactory() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            SSLSocketFactory factory = new SSLTrustAllSocketFactory(trustStore);
            return factory;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Socket createSocket(Socket socket, String host,
                               int port, boolean autoClose)
            throws IOException {
        return mCtx.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return mCtx.getSocketFactory().createSocket();
    }

    public class SSLTrustAllManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
}