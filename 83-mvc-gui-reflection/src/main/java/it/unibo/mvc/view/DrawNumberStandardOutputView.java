package it.unibo.mvc.view;


import it.unibo.mvc.api.DrawNumberController;
import it.unibo.mvc.api.DrawNumberView;
import it.unibo.mvc.api.DrawResult;

/**
 * DrawNumberStandardOutputView
 */
public class DrawNumberStandardOutputView implements DrawNumberView {

    public DrawNumberStandardOutputView() {
    }


    @Override
    public void setController(DrawNumberController observer) {

    }

    @Override
    public void start() {
       
    }

    @Override
    public void result(DrawResult res) {
        
        IO.println(res.getDescription());
    }

    
}