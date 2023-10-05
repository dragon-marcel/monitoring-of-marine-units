package backend.Marine.units.service;

import backend.Marine.units.model.Area;
import org.springframework.stereotype.Service;

@Service
public class AreaManager {
    private Area arena;

    public Area getArea() {
        return arena;
    }

    public void setArea() {
        arena = new Area(10.09094, 63.3989, 10.67047, 63.58645);
    }
}
