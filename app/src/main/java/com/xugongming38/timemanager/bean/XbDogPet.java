package com.xugongming38.timemanager.bean;

import com.xugongming38.timemanager.R;
import com.xugongming38.timemanager.base.PetAttributes;

import java.util.Random;

/**
 * Created by dell on 2018/4/19.
 */

public class XbDogPet implements PetAttributes {
    /**
     * 所有辛巴狗的动作
     */
    private static final int[] BACKGROUND_RES_ID = {
            //拜托               奔跑嗨              崇拜              高兴转圈
            R.drawable.xbdog0, R.drawable.xbdog1, R.drawable.xbdog2, R.drawable.xbdog3,
            //流口水               弹琴              挥刀无奈            微笑
            R.drawable.xbdog4, R.drawable.xbdog5, R.drawable.xbdog6, R.drawable.xbdog7,
            //哭死                  人呢              被雷打             偷笑
            R.drawable.xbdog8, R.drawable.xbdog9, R.drawable.xbdog10, R.drawable.xbdog11,
            //睡觉                吃药              赞                   撒娇
            R.drawable.xbdog12, R.drawable.xbdog13, R.drawable.xbdog14, R.drawable.xbdog15,
    };
    /**
     * 指定几种特别的动作
     */
    public static final int ACTION_PLEASE = BACKGROUND_RES_ID[0];
    public static final int ACTION_TITTER = BACKGROUND_RES_ID[11];
    public static final int ACTION_ZAN = BACKGROUND_RES_ID[14];
    public static final int ACTION_HAPPY = BACKGROUND_RES_ID[3];
    public static final int ACTION_PLAYING = BACKGROUND_RES_ID[5];
    public static final int ACTION_COQUETRY = BACKGROUND_RES_ID[15];
    public static final int ACTION_DEFAULT = BACKGROUND_RES_ID[12];

    /**
     * 宠物动作总数
     */
    private int actionSum = BACKGROUND_RES_ID.length;


    @Override
    public int getRandomAction() {
        Random random = new Random();
        int res = random.nextInt(actionSum);
        return BACKGROUND_RES_ID[res];
    }

    @Override
    public String getRandomSaying() {
        return "";
    }

    @Override
    public Pet getDeauft() {
        Pet pet = new Pet();
        pet.setAction(ACTION_DEFAULT);
        pet.setSaying("");
        return pet;
    }

    @Override
    public Pet getStudy() {
        Pet pet = new Pet();
        pet.setAction(ACTION_PLAYING);
        pet.setSaying("开始学习啦~~非系统App已经被俺禁用了,好好学习吧");
        return pet;
    }

    @Override
    public Pet getRest() {
        Pet pet = new Pet();
        pet.setAction(ACTION_DEFAULT);
        pet.setSaying("俺先睡会~~不要偷偷玩手机哦，俺会发现的");
        return pet;
    }

    @Override
    public Pet getWarning() {
        Pet pet = new Pet();
        Random random = new Random();
        int action = random.nextInt(3);
        if (action == 0){
            pet.setAction(ACTION_PLEASE);
            pet.setSaying("不要玩手机啦~~好好学习嘛");
        }else if (action == 1){
            pet.setAction(ACTION_TITTER);
            pet.setSaying("xixi~~俺看着你，看你怎么玩手机");
        } else  if (action == 2){
            pet.setAction(ACTION_COQUETRY);
            pet.setSaying("不要再玩手机啦~~");
        } else {
            pet.setAction(ACTION_DEFAULT);
            pet.setSaying("原谅你一次~~俺继续睡了，记住别玩手机了哦");
        }
        return pet;
    }

    @Override
    public Pet getFinish() {
        Pet pet = new Pet();
        Random random = new Random();
        int action = random.nextInt(2);
        if (action == 0){
            pet.setAction(ACTION_HAPPY);
            pet.setSaying("lala~~辛苦了，俺允许你玩手机了");
        }else if (action == 1){
            pet.setAction(ACTION_ZAN);
            pet.setSaying("zan~~俺越来越崇拜你了");
        } else {
            pet.setAction(ACTION_DEFAULT);
            pet.setSaying("不错哦，俺睡去了~~");
        }
        return pet;
    }
}

