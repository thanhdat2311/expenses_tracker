package vn.dathocjava.dathocjava_sample.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import vn.dathocjava.dathocjava_sample.model.User;
import vn.dathocjava.dathocjava_sample.response.PageResponse;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
public class SearchRepository {
    @PersistenceContext
    private EntityManager entityManager;
    public PageResponse<?> getAllUserBySortColumnAndSearch(int pageNo,
                                                           int pageSize,
                                                           String search,
                                                           String sortBy){
        // query ra list user
        // query ra so recore

        StringBuilder sqlQuery = new StringBuilder("select new" +
                " vn.dathocjava.dathocjava_sample.dto.response" +
                ".UserDetailResponse(u.id,u.firstName,u.lastName,u.dateOfBirth,u.email,u.phone)" +
                " from User u");
        if (StringUtils.hasLength(search)){
            sqlQuery.append(" where lower(u.firstName) like :search");
            sqlQuery.append(" or lower(u.lastName) like :search");
            sqlQuery.append(" or lower(u.email) like :search");
        }
        if (StringUtils.hasLength(sortBy)) {
            //id:asc|desc
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                sqlQuery.append(String.format(" order by u.%s %s", matcher.group(1),matcher.group(3)));
            }
        }
        Query selectQuery = entityManager.createQuery(sqlQuery.toString());
        selectQuery.setFirstResult(pageNo);
        selectQuery.setMaxResults(pageSize);

        if (StringUtils.hasLength(search)){
            selectQuery.setParameter("search","%"+ search.toLowerCase() +"%");

        }

        List<User> userList =  selectQuery.getResultList();
        StringBuilder sqlCountQuery = new StringBuilder("select count(*) from User u");
        if (StringUtils.hasLength(search)){
            sqlCountQuery.append(" where lower(u.firstName) like ?1");
            sqlCountQuery.append(" or lower(u.lastName) like ?2");
            sqlCountQuery.append(" or lower(u.email) like ?3");
        }
        Query selectCountQuery = entityManager.createQuery(sqlCountQuery.toString());
        if (StringUtils.hasLength(search)){
            selectCountQuery.setParameter(1,String.format("%%%s%%" ,search.toLowerCase()));
            selectCountQuery.setParameter(2,String.format("%%%s%%" ,search.toLowerCase()));
            selectCountQuery.setParameter(3,String.format("%%%s%%" ,search.toLowerCase()));
        }
        Long totalElements = ( Long ) selectCountQuery.getSingleResult();
        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(totalElements.intValue()/pageSize)
                .items(userList)
                .build();



    }

}
