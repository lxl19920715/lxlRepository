package org.yaukie.frame.autocode.dao.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.yaukie.frame.autocode.model.XRepository;
import org.yaukie.frame.autocode.model.XRepositoryExample;

public interface XRepositoryMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table x_repository
     *
     * @mbg.generated Mon Nov 23 19:21:47 CST 2020
     */
    long countByExample(XRepositoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table x_repository
     *
     * @mbg.generated Mon Nov 23 19:21:47 CST 2020
     */
    int deleteByExample(XRepositoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table x_repository
     *
     * @mbg.generated Mon Nov 23 19:21:47 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table x_repository
     *
     * @mbg.generated Mon Nov 23 19:21:47 CST 2020
     */
    int insert(XRepository record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table x_repository
     *
     * @mbg.generated Mon Nov 23 19:21:47 CST 2020
     */
    int insertSelective(XRepository record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table x_repository
     *
     * @mbg.generated Mon Nov 23 19:21:47 CST 2020
     */
    List<XRepository> selectByExample(XRepositoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table x_repository
     *
     * @mbg.generated Mon Nov 23 19:21:47 CST 2020
     */
    XRepository selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table x_repository
     *
     * @mbg.generated Mon Nov 23 19:21:47 CST 2020
     */
    int updateByExampleSelective(@Param("record") XRepository record, @Param("example") XRepositoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table x_repository
     *
     * @mbg.generated Mon Nov 23 19:21:47 CST 2020
     */
    int updateByExample(@Param("record") XRepository record, @Param("example") XRepositoryExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table x_repository
     *
     * @mbg.generated Mon Nov 23 19:21:47 CST 2020
     */
    int updateByPrimaryKeySelective(XRepository record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table x_repository
     *
     * @mbg.generated Mon Nov 23 19:21:47 CST 2020
     */
    int updateByPrimaryKey(XRepository record);
}