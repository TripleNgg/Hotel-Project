package org.tripleng.likesidehotel.config;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxAppClientV2;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DropboxClientConfig {

    private static String  ACCESS_TOKEN = "sl.BxNi981cSgOHH9LjaPk2mvAnEnLAMb8RVN_vcjRYNNnbu7ZQuFcvodreklt64KML0WRcwV33_bMFbnpDpsQ7uH167iNG_9GYU7Pv-MWv1ZHYAOgUHIcvV88FlmExewowJ4iIJ5dHXWg4jAno8wQ_dMA";
    @Bean
    public DbxClientV2 clientV2() throws DbxException {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/lakeside-hotel").build();

        return new DbxClientV2(config, ACCESS_TOKEN);
    }
}
