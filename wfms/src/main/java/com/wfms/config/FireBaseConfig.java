package com.wfms.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

@Configuration
public class FireBaseConfig {
    @Bean
    public FirebaseMessaging firebaseMessaging() {
        try {
            File fileConfig = new File(Objects.requireNonNull(
                    ClassLoader.getSystemResource("templates/serviceAccountKey.json").getFile()));
            FileInputStream serviceAccount =
                    new FileInputStream(fileConfig.getAbsolutePath());
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp firebaseApp = FirebaseApp.initializeApp(options);
            return FirebaseMessaging.getInstance(firebaseApp);
        } catch (FileNotFoundException e) {
            System.out.println("ERROR:not found config file data.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
