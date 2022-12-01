/*
 * Original shader from: https://www.shadertoy.com/view/3dyGW3
 */

#ifdef GL_ES
precision mediump float;
#endif

// glslsandbox uniforms
uniform float time;
uniform vec2 resolution;

// shadertoy emulation
#define iTime time
#define iResolution resolution

// Emulate some GLSL ES 3.x
#define mat2x2 mat2

// --------[ Original ShaderToy begins here ]---------- //
#define MAXSTEPS 50
#define HITTHRESHOLD .009
#define FAR 25.
#define AA 1
#define NIFS 2
#define SCALE 2.3
#define TRANSLATE 3.5

mat2x2 rot(float angle)
{
    float c=cos(angle);
    float s=sin(angle);
    return mat2x2(c,-s,
    s,c);
}

vec4 sd2d(vec2 p,float o)
{
    float time=.25*o+.6*iTime;
    float s=.5;
    p*=s;
    float RADIUS=(1.5+sin(iTime));
    int i=0;
    vec3 col=vec3(0.);
    p=p*rot(-.4*time);// twist

    for(int ii=0;ii<NIFS;ii++)
    {
        if(p.x<0.){p.x=-p.x;col.g++;}
        p=p*rot(.9*sin(time));
        if(p.y<0.){p.y=-p.y;col.b++;}
        if(p.x-p.y<0.){p.xy=p.yx;col.r++;}
        p=p*SCALE-TRANSLATE;
        p=p*rot(.3*(iTime));
        ++i;
    }

    float d=.425*(length(p)-RADIUS)*pow(SCALE,float(-i))/s;
    col/=float(NIFS);
    vec3 oc=mix(vec3(.7,col.g,.2),vec3(.2,col.r,.7),col.b);

    return vec4(oc,d);
}

vec4 map(vec3 p)
{
    return sd2d(p.xz,p.y);
}

float shadow(vec3 ro,vec3 rd)
{
    float h=0.;
    float k=3.;//shadowSmooth
    float res=1.;
    float t=.2;//bias
    for(int i=0;i<50;i++)// t < shadowMaxDist
    {
        if (t>=15.) break;
        h=map(ro+rd*t).w;
        res=min(res,k*h/t);
        if(h<HITTHRESHOLD)
        {
            break;
        }
        t=t+h;
    }
    return clamp(res+.05,0.,1.);
}
//---------------

void mainImage( out vec4 fragColor, in vec2 fragCoord )
{
    //-----------------
    //camera
    float height=-.4;
    float rot=iTime*.1;
    float dist=9.+1.*sin(.5*iTime);
    vec3 ro=dist*vec3(cos(rot),height,sin(rot));
    vec3 lookAt=vec3(0.,0.,0.);
    vec3 fw=normalize(lookAt-ro);

    vec3 right=normalize(cross(vec3(0.,1.,1.),fw));
    vec3 up=normalize(cross(fw,right));
    right=normalize(cross(up,fw));

    //light
    rot+=sin(iTime)*.2;
    vec3 lightPos=dist*vec3(cos(rot),height,sin(rot));

    //raymarch
    vec3 pos = vec3(0.),closest = vec3(0.);
    float t = 0.;
    float smallest = 0.;
    int i = 0,iAtClosest = 0;
    vec3 sdfCol = vec3(0.);
    vec3 col = vec3(0.);

    for(int x=0;x<AA;x++)
    for(int y=0;y<AA;y++)
    {
        t=0.;smallest=500.;
        vec2 o=vec2(float(x),float(y))/float(AA)-.5;
        vec2 uv=(fragCoord+o)/iResolution.xy;
        uv-=.5;
        uv.x*=iResolution.x/iResolution.y;
        vec3 rd=normalize(fw*.5+right*uv.x+up*uv.y);

        for(int ii=0;ii<MAXSTEPS;ii++)
        {
            pos=ro+rd*t;
            vec4 mr=map(pos);
            float d=mr.w;
            if(d<smallest)smallest=d;closest=pos;iAtClosest=i;sdfCol=mr.rgb;
            if(abs(d)<HITTHRESHOLD||t>FAR){break;}
            t+=d;
        }
        pos=closest;
        i=iAtClosest;
        vec3 c;
        if(t<FAR)
        {
            c=sdfCol;
            vec3 toLight=normalize(lightPos-pos);
            float s=shadow(pos,toLight);
            c*=s;
            c=mix(c,1.5*c,1.-s);
        }
        else
        {
            c=vec3(0.);
        }
        col+=c;
    }
    col/=float(AA*AA);


    fragColor = vec4 (col,t);
}
// --------[ Original ShaderToy ends here ]---------- //

void main(void)
{
    mainImage(gl_FragColor, gl_FragCoord.xy);
}