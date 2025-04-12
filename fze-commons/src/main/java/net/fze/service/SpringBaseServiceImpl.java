package net.fze.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.fze.common.data.PagingParams;
import net.fze.common.data.PagingResult;
import net.fze.domain.IOrmRepository;
import net.fze.ext.mybatis.BaseJpaMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * Spring服务基类,与BaseServiceImpl不同的是,这里使用了@Autowired注解，能根据泛型类型注入
 * @param <T>
 */
public abstract class SpringBaseServiceImpl<T> implements IBaseService<T> {
    @Autowired IOrmRepository<T> r;
    @Override
    public T findById(Serializable id) {
        return r.findById(id).orElse(null);
    }

    @Override
    public List<T> findAll() {
       return r.findBy(null);
    }

    @Override
    public void save(T e, Function<T,Serializable> f) {;
        r.save(e,f);
    }

    @Override
    public T findBy(T o) {
        return r.findOne(o).orElse(null);
    }

    @Override
    public List<T> selectListBy(T o) {
        List<T> list =  r.findBy(o);
        if(list == null){
            return Collections.emptyList();
        }
        return list;
    }

    @Override
    public PagingResult<T> selectPaging(T o, PagingParams page){
        if(r instanceof BaseJpaMapper){
            BaseJpaMapper<T> jpaMapper = (BaseJpaMapper<T>)r;
            return jpaMapper.queryPaging(new QueryWrapper<>(o), page);
        }
        return r.selectPaging(o, page.getPageIndex(), page.getPageSize());
//        throw new RuntimeException("未实现分页查询");
    }

    @Override
    public void saveAll(Iterable<T> entities,Function<T,Serializable> f) {
        entities.forEach(a->this.save(a,f));
    }

    @Override
    public int deleteById(Serializable id) {
        return r.deleteById(id);
    }

    @Override
    public int batchDelete(List<Serializable> id) {
        return r.deleteBatchIds(id);
    }
}
