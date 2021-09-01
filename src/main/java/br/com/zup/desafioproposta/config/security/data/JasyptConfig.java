package br.com.zup.desafioproposta.config.security.data;

import br.com.zup.desafioproposta.config.exception.ServidorInternoException;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.iv.RandomIvGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Optional;

public class JasyptConfig {

    // JasyptConfig não é um componente do Spring, e nem precisa ser, pois não é injetado em nenhum Controller.
    // Por isso, não conseguimos utilizar a anotação @Value.
    // Sendo assim, não há valor padrão para a variável de ambiente. Por isso, ou definimos ela.
    // Ou deixamos o valor padrão hardcoded na classe.
    private final String salt = pegarSalt();
    private final Logger logger = LoggerFactory.getLogger(JasyptConfig.class);

    // gera o hash. Aqui, não adicionamos nenhum salt randomizado.
    public String gerarHash(String documentoAHashear) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512"); // seta o algoritmo de hash
            md.reset(); // limpa qualquer valor padrão do algoritmo de hash. Não sei se é necessário

            String hashGerado = String.format("%0128x", new BigInteger(1, // formata os bytes recebidos como resultado do hashing de volta pra string
                    md.digest( // aplica o algoritmo de hash aos bytes gerados a partir do nosso documento + salt, encodados em UTF-8
                            (documentoAHashear + salt).getBytes(StandardCharsets.UTF_8))));
            logger.info("Hash gerado com sucesso");
            return hashGerado;

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Erro na geração do hash");
            return null;
        }

    }

    // auto-explicativo
    public String criptografar(String documentoACriptografar) {
        try {
            String documentoCriptografado = encryptor().encrypt(documentoACriptografar);

            logger.info("Documento criptografado com sucesso");
            return documentoCriptografado;
        }
        catch (EncryptionOperationNotPossibleException e) {
            logger.error("Erro na criptografia");
            throw new ServidorInternoException("Erro de criptografia");
        }

    }

    // auto-explicativo
    public String descriptografar(String documentoADescriptografar) {
        try {
            String documentoDescriptografado = encryptor().decrypt(documentoADescriptografar);

            logger.info("Documento descriptografado com sucesso");
            return documentoDescriptografado;
        }
        catch (EncryptionOperationNotPossibleException e) {
            logger.error("Erro na descriptografia");
            throw new ServidorInternoException(
                    "O salt da criptografia deste registro é diferente do salt vigente");
        }
    }

    // retorna um criptografador já configurado, para ser usado nos métodos criptografar e descriptografar
    public StandardPBEStringEncryptor encryptor() {
        StandardPBEStringEncryptor textEncryptor = new StandardPBEStringEncryptor();
        textEncryptor.setPassword(salt);
        textEncryptor.setAlgorithm("PBEWithHMACSHA512AndAES_256");
        textEncryptor.setIvGenerator(new RandomIvGenerator());

        return textEncryptor;
    }

    private String pegarSalt() {

        Optional<String> possivelSalt = Optional.ofNullable(System.getenv().get("JASYPT_SECRET"));

        if (possivelSalt.isEmpty()) {
            return "rm'!@N=Ke!~p8VTA2ZRK~nMDQX5Uvm!m'D&]{@Vr?G;2?XhbC:Qa#9#eMLN}x3?JR3.2zr~v)gYF^8:8>:XfB:Ww75N/emt9Yj[bQMNCWwWJ?N,nvH.<2.r~w]*e~vgak)Xv8H`MH/72E`,^k@n<vE-wD3g9JWPy;CrY*.Kd2_D])=><D?YhBaSua5hW%{2]_FVXzb9`8FH^b[X3jzVER&:jw2<=c38=>L/zBq`}C6tT*cCSVC^c]-L}&/";
        }
        return possivelSalt.get();

    }

}
