package project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertEquals;

import project.domain.Article;
import project.dto.AddArticleRequest;
import project.dto.UpdateArticleRequest;
import project.repository.BlogRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.naming.spi.DirStateFactory.Result;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BlogApiControllerTest {
    
    @Autowired
    protected MockMvc mockMvc;
    
    @Autowired
    protected ObjectMapper objectMapper;
    
    @Autowired
    private WebApplicationContext context;
    
    @Autowired
    BlogRepository blogRepository;
    
    @Before
    public void mockMvcSetUp(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .build();
        blogRepository.deleteAll();
    }
    
    @Test
    public void addArticle() throws Exception{
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);

        final String requestBody = objectMapper.writeValueAsString(userRequest);

        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        result.andExpect(status().isCreated());

        List<Article> articles = blogRepository.findAll();

        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);
    }
    
    @Test
    public void findAllArticles() throws Exception{
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        
        blogRepository.save(Article.builder()
                           .title(title)
                           .content(content)
                           .build());
        
        final ResultActions resultActions = mockMvc.perform(get(url)
                                                           .accept(MediaType.APPLICATION_JSON));
        
        resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].content").value(content))
            .andExpect(jsonPath("$[0].title").value(title));
    }
    
    @Test
    public void findArticle() throws Exception{
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";
        
        Article savedArticle = blogRepository.save(Article.builder()
                                                  .title(title)
                                                  .content(content)
                                                  .build());
        
        final ResultActions resultActions = mockMvc.perform(get(url, savedArticle.getId()));
        
        resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").value(content))
            .andExpect(jsonPath("$.title").value(title));
    }
    
    @Test
    public void deleteArticle() throws Exception{
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";
        
        Article savedArticle = blogRepository.save(Article.builder()
                                                  .title(title)
                                                  .content(content)
                                                  .build());
        
        mockMvc.perform(delete(url, savedArticle.getId()))
            .andExpect(status().isOk());
        
        List<Article> articles = blogRepository.findAll();
        
        assertThat(articles).isEmpty();
    }
    
    @Test
    public void updateArticle() throws Exception{
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";
        
        Article savedArticle = blogRepository.save(Article.builder()
                                                  .title(title)
                                                  .content(content)
                                                  .build());
        
        final String newTitle = "new title";
        final String newContent = "new content";
        
        UpdateArticleRequest request = new UpdateArticleRequest(newTitle, newContent);
        
        ResultActions result = mockMvc.perform(put(url, savedArticle.getId())
                                              .contentType(MediaType.APPLICATION_JSON_VALUE)
                                              .content(objectMapper.writeValueAsString(request)));
        
        result.andExpect(status().isOk());
        
        Article article = blogRepository.findById(savedArticle.getId()).get();
        
        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newContent);
            
    }
}
