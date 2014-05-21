from pygame import *
from glob import *
pics = glob("*.png")
for i in pics:
    pic = image.load(i)
    size=40+pic.get_width(),pic.get_height()
    screen = display.set_mode((size))
    running = 1
    while running:
        for evt in event.get():
            if evt.type==QUIT:running=0
            if evt.type==KEYDOWN:
                if evt.key==27:running=0
        screen.blit(pic,(40,0))
        
        display.flip()
    quit()
