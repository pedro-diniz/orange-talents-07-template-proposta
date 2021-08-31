package br.com.zup.desafioproposta.config.security.data;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class JasyptConfig {

    @Value("${jasypt.secret}")
    private String jasyptSecret;

    // gera o hash. Aqui, não adicionamos nenhum salt randomizado.
    public String gerarHash(String documentoAHashear) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512"); // seta o algoritmo de hash
            md.reset(); // limpa qualquer valor padrão do algoritmo de hash. Não sei se é necessário
            return String.format("%0128x", new BigInteger(1, // recebemos os bytes hasheados e convertemos pra string.
                            md.digest((documentoAHashear + jasyptSecret) // conversão propriamente dita. Só sei que convertemos a string em bytes.
                                    .getBytes(StandardCharsets.UTF_8))));

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    // auto-explicativo
    public String criptografar(String documentoACriptografar) {
        return encryptor().encrypt(documentoACriptografar);
    }

    // auto-explicativo
    public String descriptografar(String documentoADescriptografar) {
        return encryptor().decrypt(documentoADescriptografar);
    }

    // retorna um criptografador já configurado, para ser usado nos métodos criptografar e descriptografar
    public StandardPBEStringEncryptor encryptor() {
        StandardPBEStringEncryptor textEncryptor = new StandardPBEStringEncryptor();
        textEncryptor.setPassword("${jasypt.secret}");
        textEncryptor.setAlgorithm("PBEWithHMACSHA512AndAES_256");
        textEncryptor.setIvGenerator(new RandomIvGenerator());

        return textEncryptor;
    }

}
