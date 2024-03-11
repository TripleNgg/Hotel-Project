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

    @Value("${dropbox.key.access-token}")
    private static String  ACCESS_TOKEN ;
    @Bean
    public DbxClientV2 clientV2() throws DbxException {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/lakeside-hotel").build();

        return new DbxClientV2(config, ACCESS_TOKEN);
    }
}
