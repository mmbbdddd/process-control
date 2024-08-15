package cn.hz.ddbm.pc.configuration

import cn.hutool.core.lang.Assert
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = PcApp.class,properties = ["application.properties"])
class PcPropertiesTest {

//    @Mock
//    PcProperties.StatusManager statusManager
//    @Mock
//    PcProperties.SessionManager sessionManager
    @Autowired
    PcProperties pcProperties
//
//    @Before
//    void setUp() {
//        MockitoAnnotations.initMocks(this)
//    }


    @Test
    public void initProperties() {
        Assert.isTrue(pcProperties.format == "json")
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme