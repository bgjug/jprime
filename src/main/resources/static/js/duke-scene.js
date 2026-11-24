(() => {
  const canvas = document.querySelector('#duke-field');
  if (!canvas) return;
  const image = canvas.parentElement.querySelector('img');
  const ctx = canvas.getContext('2d');
  if (!ctx) return;
  const palette = Array.from({length:32}, (_, i) => {
    const t = i/31;
    return 'rgb('+Math.round(38+177*t)+','+Math.round(82+164*t)+','+Math.round(132+123*t)+')';
  });

  // The EPS supplies the front colours. A single tapered shell carries them
  // around to a tailored jacket back, independent of the arm silhouette.
  function sampleArtwork() {
    const map = document.createElement('canvas');
    map.width = 264;
    map.height = 302;
    const sampler = map.getContext('2d', {willReadFrequently:true});
    sampler.drawImage(image, 0, 0, map.width, map.height);
    const pixels = sampler.getImageData(0, 0, map.width, map.height).data;
    const points = [];
    function sample(x, y, fallback) {
      const i = 4 * (Math.round(y) * map.width + Math.round(x));
      if (pixels[i + 3] < 128) return fallback;
      return (.2*pixels[i] + .65*pixels[i+1] + .15*pixels[i+2]) / 255;
    }
    // Height, half-width and centre of Duke's body in the logo's coordinates.
    const profile = [[10,2,124],[16,10,126],[34,22,129],[65,38,131],
      [100,54,132],[127,65,132],[150,68,132],[178,78,132],
      [212,87,132],[242,92,132]];
    let segment = 0;
    for (let h=10; h<242; h+=2.4) {
      while (h>profile[segment+1][0]) segment++;
      const [top,a,ca]=profile[segment], [bottom,b,cb]=profile[segment+1];
      const u=(h-top)/(bottom-top);
      const radius=a+(b-a)*u, center=ca+(cb-ca)*u;
      const count=Math.max(12,Math.ceil(radius*2.15));
      for(let j=0;j<count;j++){
        const phi=2*Math.PI*j/count;
        const nx=Math.sin(phi), nz=Math.cos(phi);
        const x=center+radius*nx;
        const y=h;
        const seam=235+6*Math.cos(2*phi);
        const collar=155+15*Math.max(0,-nz);
        let tone=h<127?.27:h<collar?.77:h<seam?.54:.79;
        if(Math.abs(h-seam)<2.2 || Math.abs(h-collar)<2)tone=.92;
        if(nz<0 && h>collar && h<seam && Math.abs(nx)<.025)tone=.32;
        const front=Math.min(1,Math.max(0,nz*3));
        tone+=front*(sample(x,y,tone)-tone);
        const lens=Math.max(0,1-((x-132)/39)**2-((y-134)/31)**2);
        const z=radius*.70*nz+(nz>0?31*Math.sqrt(lens)*front:0);
        points.push({x:x-132,y:y-151,z,nx,nz,tone});
      }
    }
    // Read the continuous lower outline from the logo, including its central
    // arch. Wrap it around the shell and close the underside along that curve.
    const hemY=242, halfWidth=92;
    const outline=new Float32Array(2*halfWidth+1);
    for(let col=0;col<outline.length;col++){
      const x=132-halfWidth+col;
      let y=map.height-1;
      while(y>hemY && pixels[4*(y*map.width+x)+3]<128)y--;
      outline[col]=y;
    }
    for(let ring=0;ring<=36;ring++){
      const bend=ring*Math.PI/72;
      for(let j=0;j<208;j++){
        const phi=2*Math.PI*j/208, nx=Math.sin(phi), nz=Math.cos(phi);
        const x=halfWidth*nx;
        const col=x+halfWidth, i=Math.floor(col);
        const bottom=outline[i]+(outline[Math.min(i+1,outline.length-1)]-outline[i])*(col-i);
        const y=hemY+(bottom-hemY)*Math.sin(bend);
        const z=halfWidth*.70*nz*Math.cos(bend);
        const front=Math.max(0,nz);
        const tone=.76+(sample(x+132,y,.76)-.76)*front;
        points.push({x,y:y-151,z,nx,nz:nz*Math.cos(bend),tone});
      }
    }
    const shellCount=points.length;
    // Bent arms are thin tubes from shoulder to elbow to hip.
    for(const side of [-1,1]){
      const joints=[[66,158,-2],[118,190,1],[101,221,9],[91,240,13]];
      for(let k=1;k<joints.length;k++){
        const a=joints[k-1], b=joints[k], dx=b[0]-a[0], dy=b[1]-a[1];
        const length=Math.hypot(dx,dy);
        for(let d=0;d<length;d+=2){
          const t=d/length;
          const radius=k===3?5.8:3.8;
          for(let j=0;j<14;j++){
            const phi=2*Math.PI*j/14, n=Math.sin(phi), nz=Math.cos(phi);
            const nx=side*n*dy/length;
            points.push({x:side*(a[0]+dx*t)+radius*nx,
              y:a[1]+dy*t-radius*n*dx/length-151,
              z:a[2]+(b[2]-a[2])*t+radius*nz,nx,nz,tone:.55});
          }
        }
      }
    }
    // The flower is pinned to the cap, so its volume stays on the front.
    for(let i=1;i<20;i++){
      const lat=i*Math.PI/20;
      for(let j=0;j<40;j++){
        const phi=j*Math.PI/20, nx=Math.sin(lat)*Math.sin(phi), nz=Math.sin(lat)*Math.cos(phi);
        const x=83+20*nx, y=76+18*Math.cos(lat);
        points.push({x:x-132,y:y-151,z:19+12*nz,nx,nz,tone:sample(x,y,.64)});
      }
    }
    return {points,shellCount};
  }
  function start(){
    const {points,shellCount}=sampleArtwork();
    const depthWidth=140;
    const depth=new Float32Array(depthWidth*156);
    let width,height,angle=-0.10,started=0,timer=0,visible=false;
    let pointer=null, hoverStrength=0, hovering=false;
    const stage=canvas.parentElement;
    stage.addEventListener('pointermove',event=>{
      if(event.pointerType==='touch')return;
      const bounds=canvas.getBoundingClientRect();
      hovering=true;
      pointer={x:event.clientX-bounds.left-width/2,y:event.clientY-bounds.top-height/2};
    },{passive:true});
    stage.addEventListener('pointerleave',()=>{hovering=false;});
    stage.addEventListener('pointercancel',()=>{hovering=false;});
    function draw(pose=angle){
      const c=Math.cos(pose),s=Math.sin(pose),scale=Math.min(height/350,width/335);
      ctx.clearRect(0,0,width,height);
      hoverStrength+=((hovering?1:0)-hoverStrength)*.20;
      depth.fill(-Infinity);
      for(let i=0;i<points.length;i++){
        const p=points[i];
        const normal=p.nz*c-p.nx*s;
        if(normal<0)continue;
        const screenX=p.x*c+p.z*s;
        const z=p.z*c-p.x*s;
        const cell=Math.floor((p.y+156)/2)*depthWidth+Math.floor((screenX+140)/2);
        if(i<shellCount){
          depth[cell]=Math.max(depth[cell],z);
          depth[cell+1]=Math.max(depth[cell+1],z);
          depth[cell+depthWidth]=Math.max(depth[cell+depthWidth],z);
        }else if(z<depth[cell]-2)continue;
        const rim=(1-normal)**2;
        let gleam=0;
        if(pointer){
          const dx=screenX-pointer.x/scale, dy=p.y-pointer.y/scale;
          const falloff=Math.max(0,1-(dx*dx+dy*dy)/(105*105));
          gleam=.17*hoverStrength*falloff*falloff;
        }
        const light=Math.min(1,gleam+.10+p.tone*.68+rim*.27+Math.max(0,-p.nx*c-p.nz*s)*.12);
        ctx.fillStyle=palette[Math.round(light*31)];
        const size=(1.03+light*.85)*scale;
        ctx.fillRect(width*.5+(p.x*c+p.z*s)*scale,height*.50+p.y*scale,size,size);
      }
    }
    // Retain the pose when leaving the section, and resume from it on return.
    function resume(){
      if(timer){
        angle+=(performance.now()-started)*Math.PI/12000;
        clearInterval(timer);
        timer=0;
      }
      if(!visible || document.hidden)return;
      started=performance.now();
      const initial=angle;
      timer=setInterval(()=>{
        const pose=initial+(performance.now()-started)*Math.PI/12000;
        draw(pose);
      },40);
    }
    new ResizeObserver(()=>{
      width=canvas.clientWidth;height=canvas.clientHeight;
      const ratio=Math.min(1.5,devicePixelRatio);
      canvas.width=Math.round(width*ratio);canvas.height=Math.round(height*ratio);
      ctx.setTransform(ratio,0,0,ratio,0,0);draw();
    }).observe(canvas);
    new IntersectionObserver(([entry])=>{visible=entry.isIntersecting;resume();}).observe(canvas);
    document.addEventListener('visibilitychange',resume);
    canvas.hidden=false;image.hidden=true;
  }
  image.decode().then(start).catch(()=>{ /* The supplied logo remains visible. */ });
  document.querySelectorAll('#main-slide,#about,#team,#why-attend .services-item').forEach(el=>{
    if(el.id && el.id!=='main-slide')el.classList.add('jprime-atmosphere');
    el.addEventListener('pointermove',event=>{
      const box=el.getBoundingClientRect();
      el.style.setProperty('--light-x',(event.clientX-box.left)/box.width*100+'%');
      el.style.setProperty('--light-y',(event.clientY-box.top)/box.height*100+'%');
    },{passive:true});
  });
})();
