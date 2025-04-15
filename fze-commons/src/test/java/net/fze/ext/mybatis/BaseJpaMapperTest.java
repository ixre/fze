package net.fze.ext.mybatis;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.fze.domain.query.IQueryWrapper;
import net.fze.domain.query.MapQueryWrapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BaseJpaMapperTest {


    public class Foo {

    }


    public interface FooJpaMapper extends BaseJpaMapper<Foo> {
    }

    public static class FooQueryWrapper<T> extends QueryWrapper<T> implements IQueryWrapper { }

    FooJpaMapper fooJpaMapper;
    @Test
    void findBy() {
        FooQueryWrapper<Foo> fooQueryWrapper = new FooQueryWrapper<>();
        fooQueryWrapper.eq("id", 1);

        IQueryWrapper queryWrapper = fooQueryWrapper;
        Wrapper<Foo> wrapper = (Wrapper<Foo>) queryWrapper;

        assert wrapper instanceof Wrapper;

        fooJpaMapper.findBy(fooQueryWrapper);
    }
}