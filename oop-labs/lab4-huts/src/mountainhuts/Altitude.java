package mountainhuts;

public class Altitude {
    private Integer min;
    private Integer max;
    public Altitude(Integer min, Integer max){
        this.min=min;
        this.max=max;
    }

    public Integer getMin(){
        return min;
    }

    public Integer getMax(){
        return max;
    }

    public boolean isIn(Integer value){
        if (value >= min && value <= max){
            return true;
        }
        return false;
    }

    @Override
    public String toString(){
        return String.format("%s-%s",min,max);
    }


}
