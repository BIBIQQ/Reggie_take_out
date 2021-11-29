package com.ff.doto;

import com.ff.common.Result;
import com.ff.entity.Setmeal;
import com.ff.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;


}
