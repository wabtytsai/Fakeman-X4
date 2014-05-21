from pygame import *
screen = display.set_mode((800,600))
def run():
    global vert,horz,up,down,left,right
    up=down=left=right=0
    for evt in event.get():
        if evt.type==QUIT:
            return 0
        if evt.type==KEYDOWN:
            if evt.key==27:return 0
            up = evt.key==273
            down = evt.key==274
            left = evt.key==276
            right = evt.key==275
            if up:
                vert.append(-5)
            if down:
                vert.append(5)
            if right:
                horz.append(5)
            if left:
                horz.append(-5)
##        if evt.type==MOUSEBUTTONDOWN:
##        if evt.type==MOUSEBUTTONUP:
    return 1
x,y=390,290
up=down=right=left=0
vert=[]
horz=[]
while run():
    screen.fill((0,0,0))
    draw.line(screen,(255,0,0),(400,200),(300,300))
    draw.line(screen,(255,0,0),(400,200),(500,300))
    draw.line(screen,(255,0,0),(400,400),(300,300))
    draw.line(screen,(255,0,0),(400,400),(500,300))
    draw.rect(screen,(0,255,0),(x,y,20,20))
    if up:y-=5
    if down:y+=5
    if right:x+=5
    if left:x-=5
    display.flip()
quit()
print vert
print horz
