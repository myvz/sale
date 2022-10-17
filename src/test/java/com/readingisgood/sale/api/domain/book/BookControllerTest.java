package com.readingisgood.sale.api.domain.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.readingisgood.sale.api.security.JJWTAuthenticationFilterConfiguration;
import com.readingisgood.sale.api.security.JwtConfiguration;
import com.readingisgood.sale.domain.book.Book;
import com.readingisgood.sale.domain.book.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = BookController.class)
@Import(value = {JJWTAuthenticationFilterConfiguration.class, JwtConfiguration.class})
class BookControllerTest {

    @Autowired
    MockMvc mockMvc;

    @SpyBean
    BookMapper bookMapper;

    @MockBean
    BookService bookService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void createNewBook() throws Exception {
        NewBookRequest request = NewBookRequest.builder()
                .ISBN("978-1-56619-909-4")
                .author("9780132350884")
                .name("Robert Martin")
                .genre("Software Design")
                .stockCount(1000L)
                .build();

        when(bookService.createNewBook(bookMapper.mapToBook(request))).thenAnswer(i -> i.getArguments()[0]);
        mockMvc.perform(post("/book")
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJpc3MiOiJSZWFkaW5nSXNHb29kIn0.7UqcWUkjxSHgnAVf8lPBnPTs6HHUQEVGi9OWVUUvcaA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.result").value("SUCCESS"))
                .andExpect(jsonPath("$.data.name").value(request.getName()))
                .andExpect(jsonPath("$.data.genre").value(request.getGenre()))
                .andExpect(jsonPath("$.data.author").value(request.getAuthor()))
                .andExpect(jsonPath("$.data.stockCount").value(request.getStockCount()));
    }

    @Test
    void updateBook() throws Exception {
        long bookId = 123;
        var request = BookUpdateRequest.builder()
                .newStockCount(1000L)
                .build();

        var book = Book.builder().ISBN("978-1-56619-909-4")
                .author("9780132350884")
                .name("Robert Martin")
                .genre("Software Design")
                .stockCount(1000L)
                .build();

        when(bookService.updateBook(bookId, bookMapper.mapToBookUpdateDto(request))).thenReturn(book);
        mockMvc.perform(patch("/book/{bookId}", bookId)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJpc3MiOiJSZWFkaW5nSXNHb29kIn0.7UqcWUkjxSHgnAVf8lPBnPTs6HHUQEVGi9OWVUUvcaA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("SUCCESS"))
                .andExpect(jsonPath("$.data.name").value(book.getName()))
                .andExpect(jsonPath("$.data.genre").value(book.getGenre()))
                .andExpect(jsonPath("$.data.author").value(book.getAuthor()))
                .andExpect(jsonPath("$.data.stockCount").value(book.getStockCount()));
    }
}