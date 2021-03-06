package cn.xiuminglee.tools;

import cn.xiuminglee.tools.modules.home.view.HomeViewInit;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

/**
 * @author Xiuming Lee
 * @description 请启动 {@link MingToolsApplication#main(String[])}
 */
@Slf4j
public class MingToolsFXApplication extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() throws Exception {
        // 将JavaFX相关的类注册到Spring应用中去
        ApplicationContextInitializer<GenericApplicationContext> initializer = genericApplicationContext -> {
            genericApplicationContext.registerBean(Application.class,()->MingToolsFXApplication.this);
            genericApplicationContext.registerBean(Parameters.class, this::getParameters);
            genericApplicationContext.registerBean(HostServices.class,this::getHostServices);
        };

        // 构建并启动Spring应用
        this.context = new SpringApplicationBuilder()
                .sources(MingToolsApplication.class)
                .initializers(initializer)
                .run(getParameters().getRaw().toArray(new String[0]));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        HomeViewInit homeViewInit = new HomeViewInit();
        homeViewInit.initView();
    }

    @Override
    public void stop() throws Exception {
        log.info("调用了Stop方法");
        this.context.close();
        Platform.exit();
    }
}
