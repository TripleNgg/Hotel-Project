package org.tripleng.likesidehotel.config;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxAppClientV2;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DropboxClientConfig {

//    @Value("${dropbox.key.access-token}")
    private static String  ACCESS_TOKEN  = "sl.BxPzwa-P8lvmDabypnfaeZfsJ-ArVWJc1kVKPKlbUsMHHO1-XV1h1HFk5u7hymnwhAOMdMX4cLs9QM7y-o4jcOFvK8GBWx5xHF5VyZXZYktUnlZEFI5WuVB2BBGIB3dOP_c_75LjIi2za_8y_5pf1Ug";
    @Bean
    public DbxClientV2 clientV2() throws DbxException {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/lakeside-hotel").build();
        System.out.println(ACCESS_TOKEN);
        return new DbxClientV2(config, ACCESS_TOKEN);
    }
}
