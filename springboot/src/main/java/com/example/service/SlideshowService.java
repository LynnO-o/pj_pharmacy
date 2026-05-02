package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Slideshow;
import com.example.mapper.SlideshowMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务层方法
 */
@Service
public class SlideshowService {

    @Resource
    private SlideshowMapper slideshowMapper;

    public void add(Slideshow slideshow) {
        //slideshow.setTime(DateUtil.now());
        slideshowMapper.insert(slideshow);
    }

    public void updateById(Slideshow slideshow) {
        slideshowMapper.updateById(slideshow);
    }

    public void deleteById(Integer id) {

        slideshowMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            slideshowMapper.deleteById(id);
        }
    }

    public Slideshow selectById(Integer id) {
        return slideshowMapper.selectById(id);
    }

    public List<Slideshow> selectAll(Slideshow slideshow) {
        return slideshowMapper.selectAll(slideshow);
    }

    public PageInfo<Slideshow> selectPage(Slideshow slideshow, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Slideshow> list = slideshowMapper.selectAll(slideshow);
        return PageInfo.of(list);
    }

}
