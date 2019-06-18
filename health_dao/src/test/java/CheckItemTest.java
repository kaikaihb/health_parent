import com.likai.dao.CheckItemDao;
import com.likai.pojo.CheckItem;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CheckItemTest {

    @Test
    public void fun01(){
        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext-dao.xml");
        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) ac.getBean("sqlSessionFactory");
        SqlSession sqlSession = sqlSessionFactory.openSession();
        CheckItemDao checkItemDao = sqlSession.getMapper(CheckItemDao.class);
        checkItemDao.add(new CheckItem());
    }
}
