#map generator
from pygame import *
screen = display.set_mode((1020,800))
def run():
    global mb,cur,cols
    for evt in event.get():
        if evt.type==QUIT:
            return 0
        if evt.type==KEYDOWN:
            if evt.key==27:return 0
        if evt.type==MOUSEBUTTONDOWN:
            mb[evt.button]=1
            if evt.button==4:
                cur=(cur-1)%len(cols)
            if evt.button==5:
                cur=(cur+1)%len(cols)
        if evt.type==MOUSEBUTTONUP:
            mb[evt.button]=0
    return 1
mb=[0,0,0,0,0,0]
cols=[(0,0,0),(255,255,255),(0,255,0),(255,0,0),(0,0,255),(0,255,255),(255,255,0),(255,0,255),(255, 127, 0),(150,255,4),(0,130,255)]
cur=0
grid=[[1]*100 for i in range(80)]
for i in range(80):
    grid[i][0]=0
    grid[i][-1]=0
for i in range(100):
    grid[0][i]=0
    grid[-1][i]=0
left=1
right=3
screen.fill((150,150,150))
while run():
    mx,my=mouse.get_pos()
    draw.rect(screen,(0,0,0),(0,0,1000,800))
    draw.rect(screen,cols[cur],(1000,0,20,800))
    for y in range(80):
        for x in range(100):
            rect=Rect(x*10,y*10,10,10)
            if rect.collidepoint(mx,my):
                if mb[left]:
                    grid[y][x]=cur
                if mb[right]:
                    grid[y][x]=1
            draw.rect(screen,cols[grid[y][x]],rect)
    display.flip()
quit()
out=""
for y in range(80):
    for x in range(100):
        out+=str(grid[y][x])+" "
out=out[:-1]
name=raw_input("Enter name")
if name!="":
    d=open(name+".txt",'w')
    d.write(out)
    d.close()
