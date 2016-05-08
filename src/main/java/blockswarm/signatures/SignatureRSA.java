package blockswarm.signatures;

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
