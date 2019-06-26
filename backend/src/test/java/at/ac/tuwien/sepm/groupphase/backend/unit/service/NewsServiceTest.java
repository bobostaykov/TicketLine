package at.ac.tuwien.sepm.groupphase.backend.unit.service;

import at.ac.tuwien.sepm.groupphase.backend.datatype.UserType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.DetailedNewsDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.SimpleNewsDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.news.NewsMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.news.NewsMapperImpl;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.user.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.NewsRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.implementation.NewsServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;

public class NewsServiceTest {

    private NewsServiceImpl newsService;
    private NewsRepository newsRepository;
    private UserRepository userRepository;
    private NewsMapper newsMapper;
    private UserMapper userMapper;

    private News TEST_NEWS_1;
    private News TEST_NEWS_2;
    private SimpleNewsDTO TEST_NEWS_SIMPLE_DTO_1;
    private SimpleNewsDTO TEST_NEWS_SIMPLE_DTO_2;
    private DetailedNewsDTO TEST_NEWS_DETAIL_DTO_1;
    private DetailedNewsDTO TEST_NEWS_DETAIL_DTO_2;

    private User TEST_USER_NO_READ_NEWS;
    private User TEST_USER_READ_NEWS;
    private List<News> TEST_READ_NEWS_LIST_NO_NEWS;
    private List<News> TEST_READ_NEWS_LIST_ONE_ENTRY;
    private List<News> TEST_READ_NEWS_LIST_ALL;

    /******************************************************************
     TEST VARIABLES
     ******************************************************************/
    private Long TEST_NEWS_ID_1 = 1L;
    private Long TEST_NEWS_ID_2 = 2L;
    private String TEST_NEWS_TITLE_1 = "title1";
    private String TEST_NEWS_TITLE_2 = "title2";
    private String TEST_NEWS_TEXT_1 = "TestNewsText1 will be summary";
    private String TEST_NEWS_TEXT_2 = "TestNewsText2 will be summary";
    private String TEST_NEWS_IMAGE_1 = "1";
    private String TEST_NEWS_IMAGE_2 = "2";
    private LocalDateTime TEST_NEWS_PUBLISHEDAT_1 = LocalDateTime.of(2016, 11, 13, 12, 15, 0, 0);
    private LocalDateTime TEST_NEWS_PUBLISHEDAT_2 = LocalDateTime.of(2016, 12, 12, 11, 13, 0, 0);
    private Long TEST_USER_ID = 11L;
    private String TEST_USER_USERNAME = "testusername";
    private String TEST_USER_PASSWORD = "testpassword";
    private UserType TEST_USER_TYPE = UserType.ADMIN;
    private LocalDateTime TEST_USER_SINCE =
        LocalDateTime.of(2018, 5, 25, 14, 22, 56);
    private LocalDateTime TEST_USER_LAST_LOGIN =
        LocalDateTime.of(2019, 5, 20, 22, 12, 6);

    @Before
    public void before() {
        this.newsRepository = Mockito.mock(NewsRepository.class);
        this.userRepository = Mockito.mock(UserRepository.class);
        this.newsMapper = new NewsMapperImpl();

        this.newsService = new NewsServiceImpl(this.newsRepository, this.userRepository, this.newsMapper);
        TEST_NEWS_DETAIL_DTO_1 = DetailedNewsDTO.builder()
            .id(TEST_NEWS_ID_1)
            .title(TEST_NEWS_TITLE_1)
            .text(TEST_NEWS_TEXT_1)
            .image(TEST_NEWS_IMAGE_1)
            .publishedAt(TEST_NEWS_PUBLISHEDAT_1)
            .build();
        TEST_NEWS_DETAIL_DTO_2 = DetailedNewsDTO.builder()
            .id(TEST_NEWS_ID_2)
            .title(TEST_NEWS_TITLE_2)
            .text(TEST_NEWS_TEXT_2)
            .image(TEST_NEWS_IMAGE_2)
            .publishedAt(TEST_NEWS_PUBLISHEDAT_2)
            .build();
        TEST_READ_NEWS_LIST_NO_NEWS = new ArrayList<News>();
        TEST_USER_NO_READ_NEWS = User.builder()
            .id(TEST_USER_ID)
            .username(TEST_USER_USERNAME)
            .password(TEST_USER_PASSWORD)
            .type(TEST_USER_TYPE)
            .userSince(TEST_USER_SINCE)
            .lastLogin(TEST_USER_LAST_LOGIN)
            .readNews(TEST_READ_NEWS_LIST_NO_NEWS)
            .build();
        TEST_READ_NEWS_LIST_ONE_ENTRY = new ArrayList<>();
        TEST_READ_NEWS_LIST_ONE_ENTRY.add(TEST_NEWS_1);
        TEST_USER_READ_NEWS = User.builder()
            .id(TEST_USER_ID)
            .username(TEST_USER_USERNAME)
            .password(TEST_USER_PASSWORD)
            .type(TEST_USER_TYPE)
            .userSince(TEST_USER_SINCE)
            .lastLogin(TEST_USER_LAST_LOGIN)
            .readNews(TEST_READ_NEWS_LIST_ONE_ENTRY)
            .build();
        TEST_NEWS_1 = newsMapper.detailedNewsDTOToNews(TEST_NEWS_DETAIL_DTO_1);
        TEST_NEWS_2 = newsMapper.detailedNewsDTOToNews(TEST_NEWS_DETAIL_DTO_2);
        TEST_NEWS_SIMPLE_DTO_1 = SimpleNewsDTO.builder()
            .id(TEST_NEWS_ID_1)
            .title(TEST_NEWS_TITLE_1)
            .summary(TEST_NEWS_TEXT_1)
            .publishedAt(TEST_NEWS_PUBLISHEDAT_1)
            .build();
        TEST_NEWS_SIMPLE_DTO_2 = SimpleNewsDTO.builder()
            .id(TEST_NEWS_ID_2)
            .title(TEST_NEWS_TITLE_2)
            .summary(TEST_NEWS_TEXT_2)
            .publishedAt(TEST_NEWS_PUBLISHEDAT_2)
            .build();
        TEST_READ_NEWS_LIST_ALL = new ArrayList<>();
        TEST_READ_NEWS_LIST_ALL.add(TEST_NEWS_1);
        TEST_READ_NEWS_LIST_ALL.add(TEST_NEWS_2);
    }

    @Test
    public void testFindOneById_Success(){
        /*
        Mockito.when(newsRepository.save(TEST_NEWS_1)).thenReturn(TEST_NEWS_1);
        Mockito.when(newsRepository.save(TEST_NEWS_2)).thenReturn(TEST_NEWS_2);
        Mockito.when(userRepository.save(TEST_USER)).thenReturn(TEST_USER);

         Mockito.when(newsService.publishNews(TEST_NEWS_DETAIL_DTO_1)).thenReturn(TEST_NEWS_DETAIL_DTO_1);

        //DetailedNewsDTO found = newsService.findOne(this.TEST_NEWS_ID_1, this.TEST_USER_USERNAME);
        DetailedNewsDTO res = newsService.publishNews(TEST_NEWS_DETAIL_DTO_1);

        List<SimpleNewsDTO> result = newsService.findUnread(TEST_USER_USERNAME);
        Assert.assertEquals(1, result);
         */
        Mockito.when(newsRepository.findOneById(TEST_NEWS_ID_1)).thenReturn(Optional.ofNullable(TEST_NEWS_1));
        DetailedNewsDTO result = newsService.findOne(TEST_NEWS_ID_1, TEST_USER_USERNAME);
        assertEquals(result.getText(), TEST_NEWS_TEXT_1);
    }

    @Test (expected = NotFoundException.class)
    public void testFindOneById_ThrowsNotFoundException() throws NotFoundException {
        Mockito.when(newsRepository.findOneById(111L)).thenThrow(NotFoundException.class);
        DetailedNewsDTO result = newsService.findOne(111L, TEST_USER_USERNAME);
        fail("NotFoundException expected but not thrown");
        verify(newsRepository).findOneById(111L);
    }

    @Test
    public void testFindeOnyByIdWithExistingUser_Successfull() {
        Mockito.when(newsRepository.findOneById(TEST_NEWS_ID_1)).thenReturn(Optional.ofNullable(TEST_NEWS_1));
        Mockito.when(userRepository.findOneByUsername(TEST_USER_USERNAME)).thenReturn(Optional.ofNullable(TEST_USER_NO_READ_NEWS));
        Mockito.when(newsRepository.getOne(TEST_NEWS_ID_1)).thenReturn(TEST_NEWS_1);
        Mockito.when(userRepository.save(TEST_USER_READ_NEWS)).thenReturn(TEST_USER_READ_NEWS);
        DetailedNewsDTO result = newsService.findOne(TEST_NEWS_ID_1, TEST_USER_USERNAME);
        assertEquals(result.getText(), TEST_NEWS_TEXT_1);
    }
}
