package net.fze.common;

import java.util.function.Function;
import java.util.function.Supplier;

public class CatchResult<T> {
    private Throwable ex ;
    private T t ;
    private boolean excepted;

    public CatchResult(Throwable ex,T t){
        this.ex = ex;
        this.t = t;
    }

    // 处理错误
    public CatchResult<T> except(Function<Throwable,Void> p)  {
        this.excepted = true;
        if (this.ex != null) p.apply(this.ex);
        return this;
    }

    /** 获取错误 */
    public Error error() {
        if (this.ex != null) return new Error(this.ex.getMessage(),this.ex);
        if (this.t instanceof Error) return (Error)this.t;
        if (this.t instanceof Throwable) {
            Throwable ta = (Throwable) this.t ;
            return new Error(ta.getMessage(), ta);
        }
        return null;
    }

    /** 应用错误回调, 如果无异常或错误则直接返回null */
    public Error applyError(Function<Error,Void> f) {
        Error err = this.error();
        if (err != null) f.apply(err);
        return err;
    }

    // 执行操作
    public CatchResult<T> then(Function<T,Void> p)  {
        this.checkThrow();
        p.apply(this.t);
        return this;
    }

    // 如果没有值,返回默认d
    public T or( T d) {
        this.checkThrow();
        return this.t != null? this.t: d;
    }

    // 返回值
    public T unwrap(){
       this.checkThrow();
        return this.t;
    }

    // 抛出异常
    public void checkThrow() {
        if (this.ex != null && !this.excepted) {
            this.ex.printStackTrace();
            throw new Error(this.ex.getMessage());
        }
    }
}
