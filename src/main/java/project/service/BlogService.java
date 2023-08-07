package project.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.domain.Article;
import project.dto.AddArticleRequest;
import project.dto.UpdateArticleRequest;
import project.repository.BlogRepository;

@RequiredArgsConstructor
@Service
public class BlogService {
    
    private final BlogRepository blogRepository;
    
    public Article save(AddArticleRequest request){
        return blogRepository.save(request.toEntity());
    }
    
    public List<Article> findAll(){
        return blogRepository.findAll();
    }
    
    public Article findById(long id){
        return blogRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
    }
    
    public void delete(long id){
        blogRepository.deleteById(id);
    }
    
    @Transactional
    public Article update(long id, UpdateArticleRequest request){
        Article article = blogRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("not found: " + id));
        
        article.update(request.getTitle(), request.getContent());
        
        return article;
    }
}
