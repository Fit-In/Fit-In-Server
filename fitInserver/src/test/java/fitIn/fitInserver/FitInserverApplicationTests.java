//package fitIn.fitInserver;
//
//import fitIn.fitInserver.controller.HelloController;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
//@RunWith(SpringRunner.class)
//@WebMvcTest(controllers = HelloController.class)
//class FitInserverApplicationTests {
//
//	@Autowired
//	private MockMvc mvc;
//
//	@Test
//	public void hello가_리턴된다() throws Exception{
//		String hello = "hello";
//
//		mvc.perform(get("/hello"))
//				.andExpect(status().isOk())
//				.andExpect(content().string(hello));
//	}
//
//}
