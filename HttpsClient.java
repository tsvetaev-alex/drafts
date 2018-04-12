package core;

import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.io.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;

import javax.net.ssl.*;

public class HttpsClient{

    public static void main(String[] args)
    {
        new HttpsClient().testIt();
    }

    // "GOOGLE" solutions;
    // # 1: disableCertificateVerification(); line 135

    // # 2: fixUntrustedCertificate(); line 173

    // # 3: line 207
    // # 3: HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
    // # 3: con.setDefaultHostnameVerifier ((hostname, session) -> true);


    @Test
    public void keyStore2() throws KeyStoreException {
        try {
            String sKeyStoreType = KeyStore.getDefaultType();
            //KeyStore ksCACert = KeyStore.getInstance(KeyStore.getDefaultType());
            KeyStore keystore = KeyStore.getInstance(sKeyStoreType);
            System.out.println("KeyStore keystore: " + keystore);
            System.out.println("KeyStore keystore.getType(): " + keystore.getType());
            keystore.load(new FileInputStream("reporter_certificate.crt"), "changeit".toCharArray());
            //ksCACert.load(new FileInputStream("reporter_certificate.crt"), "changeit".toCharArray());
            for (String alias : Collections.list(keystore.aliases()))
                System.out.println(alias);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException: " + e.getMessage());
        } catch (CertificateException e) {
            System.out.println("CertificateException: " + e.getMessage());
        } catch (KeyStoreException e) {
            System.out.println("KeyStoreException: " + e.getMessage());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void keyStore()
    {
        try{
        //String keyStoreType = java.security.KeyStore.getDefaultType();
        String current = new java.io.File( "." ).getCanonicalPath();
        System.out.println("Current dir:"+ current);
        File certificateFile = new File("reporter_certificate.crt");
        if(certificateFile.exists() && !certificateFile.isDirectory()) System.out.println("File: reporter_certificate.crt exists directory: " + current);

            KeyStore ksCACert = KeyStore.getInstance(KeyStore.getDefaultType());
            System.out.println("ksCACert : " + ksCACert.toString());

            FileInputStream fis = new FileInputStream(certificateFile);
            ksCACert.load(new FileInputStream("reporter_certificate.crt"), "changeit".toCharArray());
            fis.close();
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (KeyStoreException e) {
            System.out.println("KeyStoreException: " + e.getMessage());
        } catch (CertificateException e) {
            System.out.println("CertificateException: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException: " + e.getMessage());
        }
    }

    @Test
    public void Test_https_using_ctr() {
        try {
            String https_url = "https://epbyminw2301.minsk.epam.com:9444/kwl/temlates/000";
            URL url = new URL(https_url);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            KeyStore ksCACert = KeyStore.getInstance(KeyStore.getDefaultType());
            String current = new java.io.File( "." ).getCanonicalPath();
            System.out.println("Current dir:"+current);
            File f = new File("reporter_certificate.crt");
            if(f.exists() && !f.isDirectory()) {
                System.out.println("File: reporter_certificate.crt exists directory: " + current);
            }
            ksCACert.load(new FileInputStream("reporter_certificate.crt"), "keystorepass".toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(ksCACert);
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);
            SSLSocketFactory sslSocketFactory = context.getSocketFactory();
            urlConnection.setSSLSocketFactory(sslSocketFactory);
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String res = "";
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                res += inputLine;
            }
            in.close();
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException: " + e.getMessage());
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException: " + e.getMessage());
        } catch (KeyStoreException e) {
            System.out.println("KeyStoreException: " + e.getMessage());
        } catch (KeyManagementException e) {
            System.out.println("KeyManagementException: " + e.getMessage());
        } catch (CertificateException e) {
            System.out.println("CertificateException: " + e.getMessage());
        }
    }



    @Test
    public void testIt(){

        String https_url = "https://epbyminw2301.minsk.epam.com:9444/kwl/temlates/000";
        URL url;
        try {

            url = new URL(https_url);
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();

            //System.out.println("dumpl all cert info");
            print_https_cert(con);

            System.out.println("dump all the content");
            //dump all the content
            print_content(con);

        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }





    private void print_https_cert(HttpsURLConnection con) {

      if (con != null) {
            System.out.println("connection != null");
            try {
                //System.out.println("con.setDefaultHostnameVerifier ((hostname, session) -> true);");
                con.setDefaultHostnameVerifier ((hostname, session) -> true); //GOOGLE SOLUTION #3

                //System.out.print("GOOGLE SOLUTION # 1");
                //disableCertificateVerification();//GOOGLE SOLUTION # 1
                //javax.net.ssl.SSLHandshakeException: java.security.cert.CertificateException: No name matching epbyminw2301.minsk.epam.com found
                //System.out.println("con.getLocalCertificates(): " + con.getLocalCertificates().toString());
                //System.out.println("con.getServerCertificates(): " + con.getServerCertificates().toString());
                System.out.println("con.getContent(): " + con.getContent());

                System.out.print("Get Response Code : ");
                System.out.println(con.getResponseCode());
                System.out.println("\n");
                System.out.println("Cipher Suite : " + con.getCipherSuite());
                System.out.println("\n");

                System.out.print("Certificate[] certs = con.getServerCertificates();");
                Certificate[] certs = con.getServerCertificates();
                for (Certificate cert : certs) {
                    System.out.println("Cert Type : " + cert.getType());
                    System.out.println("Cert Hash Code : " + cert.hashCode());
                    System.out.println("Cert Public Key Algorithm : "
                            + cert.getPublicKey().getAlgorithm());
                    System.out.println("Cert Public Key Format : "
                            + cert.getPublicKey().getFormat());
                    System.out.println("\n");
                }

            } catch (SSLPeerUnverifiedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



            private void print_content (HttpsURLConnection con){
                if (con != null) {

                    try {
                        //print_https_cert(con);
                        System.out.println("****** Content of the URL ********");
                        BufferedReader br =
                                new BufferedReader(
                                        new InputStreamReader(con.getInputStream()));

                        String input;

                        while ((input = br.readLine()) != null) {
                            System.out.println(input);
                        }
                        br.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //GOOGLE CERTIFICATE SOLUTIONS//
            //javax.net.ssl.SSLHandshakeException: java.security.cert.CertificateException: No name matching epbyminw2301.minsk.epam.com found
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //GOOGLE SOLUTION # 1
    public static void disableCertificateVerification() {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
                    throws CertificateException {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
                    throws CertificateException {
            }
        }
        };

        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });
        } catch (NoSuchAlgorithmException | KeyManagementException e) {

        }
    }


    //GOOGLE SOLUTION #2
    //fixUntrustedCertificate(){
    //so when I am dealing with a domain that is not in trusted CAs you can invoke the method before the request.
    // This code will gonna work after java1.4. This method applies for all hosts:
    public void fixUntrustedCertificate() throws KeyManagementException, NoSuchAlgorithmException {

        System.out.println("INSIDE fixUntrustedCertificate BEGIN");

        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }

                }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // set the  allTrusting verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        System.out.println("INSIDE fixUntrustedCertificate END");
    }

    //GOOGLE SOLUTION # 3:
    //HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
    //con.setDefaultHostnameVerifier ((hostname, session) -> true);

}