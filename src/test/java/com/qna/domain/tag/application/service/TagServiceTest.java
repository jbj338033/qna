package com.qna.domain.tag.application.service;

import com.qna.domain.tag.application.mapper.TagMapper;
import com.qna.domain.tag.domain.entity.Tag;
import com.qna.domain.tag.domain.repository.TagRepository;
import com.qna.domain.tag.presentation.response.TagResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @InjectMocks
    private TagServiceImpl tagService;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TagMapper tagMapper;

    private Tag tag1;
    private Tag tag2;

    @BeforeEach
    void setUp() {
        tag1 = Tag.builder().name("java").build();
        ReflectionTestUtils.setField(tag1, "id", 1L);

        tag2 = Tag.builder().name("spring").build();
        ReflectionTestUtils.setField(tag2, "id", 2L);
    }

    @Test
    @DisplayName("전체 태그 조회 성공")
    void getAllTags_Success() {
        Object[] result1 = {tag1, 10L};
        Object[] result2 = {tag2, 5L};
        List<Object[]> results = Arrays.asList(result1, result2);

        TagResponse response1 = TagResponse.builder().id(1L).name("java").questionCount(10L).build();
        TagResponse response2 = TagResponse.builder().id(2L).name("spring").questionCount(5L).build();

        given(tagRepository.findAllWithQuestionCount()).willReturn(results);
        given(tagMapper.toResponse(tag1, 10L)).willReturn(response1);
        given(tagMapper.toResponse(tag2, 5L)).willReturn(response2);

        List<TagResponse> tags = tagService.getAllTags();

        assertThat(tags).hasSize(2);
        assertThat(tags.get(0).getName()).isEqualTo("java");
        assertThat(tags.get(0).getQuestionCount()).isEqualTo(10L);
        assertThat(tags.get(1).getName()).isEqualTo("spring");
        assertThat(tags.get(1).getQuestionCount()).isEqualTo(5L);
    }

    @Test
    @DisplayName("태그가 없을 때 빈 리스트 반환")
    void getAllTags_Empty() {
        given(tagRepository.findAllWithQuestionCount()).willReturn(Collections.emptyList());

        List<TagResponse> tags = tagService.getAllTags();

        assertThat(tags).isEmpty();
    }
}
