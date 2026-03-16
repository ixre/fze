package net.fze.ext.rbac.scope;

import net.fze.ext.mybatis.MyBatisLambdaQueryWrapper;
import net.fze.ext.mybatis.MyBatisQueryWrapper;
import net.fze.util.Strings;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据权限过滤切面，适配MyBatisQueryWrapper
 * 用于拦截使用MyBatisQueryWrapper的查询方法，动态添加数据权限条件
 * @author jarrysix
 *
 *
 */
// @Aspect
// @Component
public abstract class AbstractDataScopeAspectForQueryWrapper {
    public abstract User getCurrentUser();

    public abstract String getDepartAndChildrenSql(long departId);

    public abstract String getCustomDataScopeSql(long userId);

    @Before(value =  "@annotation(net.fze.ext.rbac.scope.DataScope)")
    public void doBefore(JoinPoint point){
        handleDataScope(point,getDataScopeAnnotation(point));
    }

    /**
     * 获取数据范围注解参数
     * @param joinPoint 切点
     * @return 数据范围注解参数
     */
    private DataScopeFields getDataScopeAnnotation(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DataScope annotation = method.getAnnotation(DataScope.class);
        if (annotation == null) {
            throw new RuntimeException(joinPoint.getSignature().getDeclaringTypeName()
                    + " cannot find @DataScope annotation");
        }
        return new DataScopeFields(annotation.departField(), annotation.userField());
    }

    protected void handleDataScope(final JoinPoint joinPoint,DataScopeFields dataScopeFields) {
       // 获取当前的用户
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            // 如果是超级管理员，则不过滤数据
            if (!currentUser.isAdministrator()) {
                applyDataScopeFilter(joinPoint, currentUser,dataScopeFields);
            }
        }
    }


    /**
     * 应用数据范围过滤
     *
     * @param joinPoint 切点
     * @param user      用户
     * @param fields 数据范围字段
     */
    protected void applyDataScopeFilter(JoinPoint joinPoint, User user,DataScopeFields fields) {
        // 获取方法参数
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }

        // 部门权限字段
        String finalDeptField = !Strings.isNullOrEmpty(fields.getDepartField()) ? fields.getDepartField() : "depart_id";
        // 用户权限字段
        String finalUserField = !Strings.isNullOrEmpty(fields.getUserField()) ? fields.getUserField() : "user_id";

        // 查找 MyBatisQueryWrapper 或 MyBatisLambdaQueryWrapper 参数
        MyBatisQueryWrapper<?> queryWrapper = null;
        for (Object arg : args) {
            if (arg instanceof MyBatisQueryWrapper<?> || arg instanceof MyBatisLambdaQueryWrapper) {
                assert arg instanceof MyBatisQueryWrapper<?>;
                queryWrapper = (MyBatisQueryWrapper<?>) arg;
                break;
            }
        }

        if (queryWrapper == null) {
            return;
        }


        // 获取用户角色的数据权限
        List<DataScopes> dataScopes = new ArrayList<>();
        for (Role role : user.getRoles()) {
            DataScopes dataScope = role.getDataScope();
            if (DataScopes.DATA_SCOPE_ALL == dataScope) {
                // 拥有全部权限，直接返回
                return;
            }
            dataScopes.add(dataScope);
        }

        // 如果没有数据权限配置，默认仅本人数据权限
        if (dataScopes.isEmpty()) {
            dataScopes.add(DataScopes.DATA_SCOPE_SELF);
        }

        // 构建数据权限条件
        StringBuilder sqlString = new StringBuilder();



        for (DataScopes dataScope : dataScopes) {
            if (DataScopes.DATA_SCOPE_CUSTOM.equals(dataScope)) {
                // 自定义数据权限(角色关联部门)
                String sql = this.getCustomDataScopeSql(user.getUserId());
                if(Strings.isNullOrEmpty(sql)){
                    // 默认查询角色关联部门
                    sql = String.format("SELECT dept_id FROM sys_role_dept WHERE role_id IN (SELECT role_id FROM sys_user_role WHERE user_id = %s)", user.getUserId());
                }
                sqlString.append(String.format(" OR %s IN (%s)", finalDeptField, sql));
            } else if (DataScopes.DATA_SCOPE_DEPT.equals(dataScope)) {
                // 部门数据权限
                sqlString.append(String.format(" OR %s = %s", finalDeptField, user.getDepartId()));
            } else if (DataScopes.DATA_SCOPE_DEPT_AND_CHILD.equals(dataScope)) {
                // 部门及以下数据权限
                String sql = this.getDepartAndChildrenSql(user.getDepartId());
                if(Strings.isNullOrEmpty(sql)){
                    // 默认查询下级和下级部门
                    sql = String.format("SELECT dept_id FROM sys_dept WHERE dept_id = %s OR find_in_set(%s, ancestors)", user.getDepartId(), user.getDepartId());
                }
                sqlString.append(String.format(" OR %s IN (%s)", finalDeptField, sql));
            } else if (DataScopes.DATA_SCOPE_SELF.equals(dataScope)) {
                // 仅本人数据权限
                sqlString.append(String.format(" OR %s = %s", finalUserField, user.getUserId()));
            }
        }

        if (sqlString.length() > 0) {
            // 移除第一个OR
            String condition = sqlString.substring(4);
//            boolean isEmptyQuery = queryWrapper.toQueryWrapper().isEmptyOfWhere();
//            // 使用apply方法添加原生SQL条件, apply会自动添加Where关键字
//            queryWrapper.apply(String.format(isEmptyQuery?" WHERE (%s)":" AND (%s)", condition));
            queryWrapper.apply(String.format("(%s)", condition));
        }
    }
}