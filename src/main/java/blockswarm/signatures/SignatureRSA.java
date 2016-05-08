package blockswarm.signatures;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.misc.BASE64Encoder;

/**
 *
 * @author cal
 */
public class SignatureRSA
{

    public static KeyPair generateKeyPair()
    {
        try
        {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            return kpg.genKeyPair();
        } catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(SignatureRSA.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void saveKeyPair(String path, KeyPair keyPair)
    {
        FileOutputStream fos = null;
        try
        {
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();
            // Store Public Key.
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
                    publicKey.getEncoded());
            fos = new FileOutputStream(path + "/public.key");
            fos.write(x509EncodedKeySpec.getEncoded());
            fos.close();
            // Store Private Key.
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
                    privateKey.getEncoded());
            fos = new FileOutputStream(path + "/private.key");
            fos.write(pkcs8EncodedKeySpec.getEncoded());
            fos.close();
        } catch (FileNotFoundException ex)
        {
            Logger.getLogger(SignatureRSA.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(SignatureRSA.class.getName()).log(Level.SEVERE, null, ex);
        } finally
        {
            try
            {
                fos.close();
            } catch (IOException ex)
            {
                Logger.getLogger(SignatureRSA.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static KeyPair loadKeyPair(String path)
    {
        FileInputStream fis = null;
        try
        {
            // Read Public Key.
            File filePublicKey = new File(path + "/public.key");
            fis = new FileInputStream(path + "/public.key");
            byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
            fis.read(encodedPublicKey);
            fis.close();
            // Read Private Key.
            File filePrivateKey = new File(path + "/private.key");
            fis = new FileInputStream(path + "/private.key");
            byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
            fis.read(encodedPrivateKey);
            fis.close();
            // Generate KeyPair.
            KeyFactory keyFactory = KeyFactory.getInstance("SHA1WithRSA");
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
            return new KeyPair(publicKey, privateKey);
        } catch (FileNotFoundException ex)
        {
            Logger.getLogger(SignatureRSA.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException ex)
        {
            Logger.getLogger(SignatureRSA.class.getName()).log(Level.SEVERE, null, ex);
        } finally
        {
            try
            {
                fis.close();
            } catch (IOException ex)
            {
                Logger.getLogger(SignatureRSA.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public static byte[] sign(byte[] data, byte[] privateKeyBytes)
    {
        try
        {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));

            Signature sig = Signature.getInstance("SHA1WithRSA");
            sig.initSign(privateKey);
            sig.update(data);
            byte[] signatureBytes = sig.sign();
            System.out.println("Singature:" + new BASE64Encoder().encode(signatureBytes));

            return signatureBytes;
        } catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(Signature.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SignatureException ex)
        {
            Logger.getLogger(SignatureRSA.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex)
        {
            Logger.getLogger(SignatureRSA.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex)
        {
            Logger.getLogger(SignatureRSA.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static boolean verify(byte[] data, byte[] sig, byte[] publicKeyBytes)
    {
        try
        {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey publicKey = kf.generatePublic(new X509EncodedKeySpec(publicKeyBytes));

            Signature signature = Signature.getInstance("SHA1WithRSA");

            signature.initVerify(publicKey);
            signature.update(data);

            return signature.verify(sig);
        } catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(SignatureRSA.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex)
        {
            Logger.getLogger(SignatureRSA.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex)
        {
            Logger.getLogger(SignatureRSA.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SignatureException ex)
        {
            Logger.getLogger(SignatureRSA.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
