package com.qidian.mall.user.service.utils;

import com.central.base.util.ConstantUtil;
import com.qidian.mall.user.response.SysSourceTreeVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 树结构生成工具类
 * @Author bin
 * @Date 2020-04-09
 */
public class SysSourceTreeUtils {

    public List<SysSourceTreeVo> menuCommon;
    public List<Object> list = new ArrayList<Object>();

    public List<Object> treeSource(List<SysSourceTreeVo> menu){
        this.menuCommon = menu;
        int topParent =0;
        for (SysSourceTreeVo treeDto : menu) {
            if(treeDto.getParentId().equals(0L)){
                //先添加父级节点
                setTreeDto(treeDto);
                list.add(treeDto);
                topParent++;
            }
        }

        if(topParent==0){
            // 筛选的资源 没有目录 直接就是菜单，其父id不是0 所以要单独做树结构处理
            for (SysSourceTreeVo treeDto : menu) {
                if(ConstantUtil.DELETE_FLAG_Y.equals(treeDto.getIsParent())){
                    //先添加父级节点
                    setTreeDto(treeDto);
                    list.add(treeDto);
                }
            }
        }
        return list;
    }

    public List<SysSourceTreeVo> menuChild(Long id){
        List<SysSourceTreeVo> lists = new ArrayList<>();
        for(SysSourceTreeVo treeDto:menuCommon){
            if(id.equals(treeDto.getParentId())){
                //此父节点有孩子,添加子节点
                lists.add(treeDto);
                //为子节点赋值，并再看子节点石否仍有子节点（孙）
                // 此方法会让menuChild方法称为一个n层嵌套方法,最先进入的最后返回设置父级名
                setTreeDto(treeDto);
            }
        }
        return lists;
    }

    private void setTreeDto(SysSourceTreeVo treeDto){
        //找子节点
        List<SysSourceTreeVo> childrens = menuChild(treeDto.getId());
        if(childrens.size()>0){
            treeDto.setHasChildren(true);
        }else{
            treeDto.setHasChildren(false);
        }
        treeDto.setChildren(childrens);
    }
}