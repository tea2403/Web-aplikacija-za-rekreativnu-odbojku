package fer.zavrsni.Odbojka.domain;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public abstract class Media {
    //izvor: https://medium.com/@poojithairosha/image-uploading-with-spring-boot-firebase-cloud-storage-e5ef2fbf942d
    public String uploadFile(File file, String naziv, String folder) throws IOException {
        String tip = "media";
        String fullPath = folder + "/" + naziv;

        BlobId blobId = BlobId.of("odbojka-c4851.appspot.com", fullPath);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(tip).build();
        InputStream inputStream = Media.class.getClassLoader().getResourceAsStream("odbojka-c4851-firebase-adminsdk-uitnw-f719cfb960.json");
        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));

        return String.format("https://firebasestorage.googleapis.com/v0/b/odbojka-c4851.appspot.com/o/%s?alt=media", URLEncoder.encode(fullPath, StandardCharsets.UTF_8));
    }

    public String upload(MultipartFile slika, String naziv, String folder){
        String x = "";
        try {
            x = slika.getOriginalFilename();
            x = naziv.concat(x.substring(x.lastIndexOf(".")));
            File fajl = new File(x);
            try (FileOutputStream fos = new FileOutputStream(fajl)) {
                fos.write(slika.getBytes());
            }
            uploadFile(fajl, x, folder);
            fajl.delete();
        } catch (Exception e) {
            Assert.hasText("", "Error");
        }
        return x;
    }

    public void izbrisi(String naziv, String folder) {
        try {
            InputStream inputStream = Media.class.getClassLoader().getResourceAsStream("odbojka-c4851-firebase-adminsdk-uitnw-f719cfb960.json");
            Credentials credentials = GoogleCredentials.fromStream(inputStream);
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

            BlobId blobId = BlobId.of("odbojka-c4851.appspot.com", folder + "/" + naziv);

            Blob blob = storage.get(blobId);
            if (blob != null) {
                blob.delete();
            }
        } catch (Exception e) {
            Assert.hasText("", "Error");
        }
    }

    public byte[] preuzmi(String naziv, String folder) throws IOException {
        InputStream inputStream = Media.class.getClassLoader().getResourceAsStream("odbojka-c4851-firebase-adminsdk-uitnw-f719cfb960.json");
        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        BlobId blobId = BlobId.of("odbojka-c4851.appspot.com", folder + "/" + naziv);
        Blob blob = storage.get(blobId);
        return blob.getContent();
    }
}
