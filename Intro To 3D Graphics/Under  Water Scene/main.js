
var canvas;
var gl;

var program ;

var near = -100;
var far = 100;


var left = -6.0;
var right = 6.0;
var ytop =6.0;
var bottom = -6.0;


var lightPosition2 = vec4(100.0, 100.0, 100.0, 1.0 );
var lightPosition = vec4(0.0, 0.0, 100.0, 1.0 );

var lightAmbient = vec4(0.2, 0.2, 0.2, 1.0 );
var lightDiffuse = vec4( 1.0, 1.0, 1.0, 1.0 );
var lightSpecular = vec4( 1.0, 1.0, 1.0, 1.0 );

var materialAmbient = vec4( 1.0, 0.0, 1.0, 1.0 );
var materialDiffuse = vec4( 1.0, 0.8, 0.0, 1.0 );
var materialSpecular = vec4( 0.4, 0.4, 0.4, 1.0 );
var materialShininess = 30.0;

var ambientColor, diffuseColor, specularColor;

var modelMatrix, viewMatrix, modelViewMatrix, projectionMatrix, normalMatrix;
var modelViewMatrixLoc, projectionMatrixLoc, normalMatrixLoc;
var eye;
var at = vec3(0.0, 0.0, 0.0);
var up = vec3(0.0, 1.0, 0.0);

var RX = 0 ;
var RY = 0 ;
var RZ = 0 ;

var MS = [] ; // The modeling matrix stack
var TIME = 0.0 ; // Realtime
var prevTime = 0.0 ;
var resetTimerFlag = true ;
var animFlag = false ;
var controller ;

function setColor(c)
{
    ambientProduct = mult(lightAmbient, c);
    diffuseProduct = mult(lightDiffuse, c);
    specularProduct = mult(lightSpecular, materialSpecular);

    gl.uniform4fv( gl.getUniformLocation(program,
                                         "ambientProduct"),flatten(ambientProduct) );
    gl.uniform4fv( gl.getUniformLocation(program,
                                         "diffuseProduct"),flatten(diffuseProduct) );
    gl.uniform4fv( gl.getUniformLocation(program,
                                         "specularProduct"),flatten(specularProduct) );
    gl.uniform4fv( gl.getUniformLocation(program,
                                         "lightPosition"),flatten(lightPosition) );
    gl.uniform1f( gl.getUniformLocation(program,
                                        "shininess"),materialShininess );
}

window.onload = function init() {

    canvas = document.getElementById( "gl-canvas" );

    gl = WebGLUtils.setupWebGL( canvas );
    if ( !gl ) { alert( "WebGL isn't available" ); }

    gl.viewport( 0, 0, canvas.width, canvas.height );
    gl.clearColor( 0.5, 0.5, 1.0, 1.0 );

    gl.enable(gl.DEPTH_TEST);

    //
    //  Load shaders and initialize attribute buffers
    //
    program = initShaders( gl, "vertex-shader", "fragment-shader" );
    gl.useProgram( program );


    setColor(materialDiffuse) ;

    Cube.init(program);
    Cylinder.init(9,program);
    Cone.init(9,program) ;
    Sphere.init(36,program) ;


    modelViewMatrixLoc = gl.getUniformLocation( program, "modelViewMatrix" );
    normalMatrixLoc = gl.getUniformLocation( program, "normalMatrix" );
    projectionMatrixLoc = gl.getUniformLocation( program, "projectionMatrix" );


    gl.uniform4fv( gl.getUniformLocation(program,
       "ambientProduct"),flatten(ambientProduct) );
    gl.uniform4fv( gl.getUniformLocation(program,
       "diffuseProduct"),flatten(diffuseProduct) );
    gl.uniform4fv( gl.getUniformLocation(program,
       "specularProduct"),flatten(specularProduct) );
    gl.uniform4fv( gl.getUniformLocation(program,
       "lightPosition"),flatten(lightPosition) );
    gl.uniform1f( gl.getUniformLocation(program,
       "shininess"),materialShininess );


/*	document.getElementById("sliderXi").oninput = function() {
		RX = this.value ;
		window.requestAnimFrame(render);
	}*/


  /*  document.getElementById("sliderYi").oninput = function() {
        RY = this.value;
        window.requestAnimFrame(render);
    };*/
    /*document.getElementById("sliderZi").oninput = function() {
        RZ =  this.value;
        window.requestAnimFrame(render);
    };*/

  /*  document.getElementById("animToggleButton").onclick = function() {
        if( animFlag ) {
            animFlag = false;
        }
        else {
            animFlag = true  ;
            resetTimerFlag = true ;
            window.requestAnimFrame(render);
        }*/


		/*controller = new CameraController(canvas);
		controller.onchange = function(xRot,yRot) {
			RX = xRot ;
			RY = yRot ;
			window.requestAnimFrame(render); };
    };*/

    render();
    animFlag = true  ;
    resetTimerFlag = true ;
    window.requestAnimFrame(render);
    console.log(animFlag);
}


// Sets the modelview and normal matrix in the shaders
function setMV() {
    modelViewMatrix = mult(viewMatrix,modelMatrix) ;
    gl.uniformMatrix4fv(modelViewMatrixLoc, false, flatten(modelViewMatrix) );
    normalMatrix = inverseTranspose(modelViewMatrix) ;
    gl.uniformMatrix4fv(normalMatrixLoc, false, flatten(normalMatrix) );
}

// Sets the projection, modelview and normal matrix in the shaders
function setAllMatrices() {
    gl.uniformMatrix4fv(projectionMatrixLoc, false, flatten(projectionMatrix) );
    setMV() ;

}

// Draws a 2x2x2 cube center at the origin
// Sets the modelview matrix and the normal matrix of the global program
function drawCube() {
    setMV() ;
    Cube.draw() ;
}

// Draws a sphere centered at the origin of radius 1.0.
// Sets the modelview matrix and the normal matrix of the global program
function drawSphere() {
    setMV() ;
    Sphere.draw() ;
}
// Draws a cylinder along z of height 1 centered at the origin
// and radius 0.5.
// Sets the modelview matrix and the normal matrix of the global program
function drawCylinder() {
    setMV() ;
    Cylinder.draw() ;
}

// Draws a cone along z of height 1 centered at the origin
// and base radius 1.0.
// Sets the modelview matrix and the normal matrix of the global program
function drawCone() {
    setMV() ;
    Cone.draw() ;
}


// Post multiples the modelview matrix with a translation matrix
// and replaces the modeling matrix with the result
function gTranslate(x,y,z) {
    modelMatrix = mult(modelMatrix,translate([x,y,z])) ;
}

// Post multiples the modelview matrix with a rotation matrix
// and replaces the modeling matrix with the result
function gRotate(theta,x,y,z) {
    modelMatrix = mult(modelMatrix,rotate(theta,[x,y,z])) ;
}

// Post multiples the modelview matrix with a scaling matrix
// and replaces the modeling matrix with the result
function gScale(sx,sy,sz) {
    modelMatrix = mult(modelMatrix,scale(sx,sy,sz)) ;
}

// Pops MS and stores the result as the current modelMatrix
function gPop() {
    modelMatrix = MS.pop() ;
}

// pushes the current modelViewMatrix in the stack MS
function gPush() {
    MS.push(modelMatrix) ;
}



function render() {

    gl.clear( gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);

    eye = vec3(0,0,10);
    MS = [] ; // Initialize modeling matrix stack

	// initialize the modeling matrix to identity
    modelMatrix = mat4() ;

    // set the camera matrix
    viewMatrix = lookAt(eye, at , up);

    // set the projection matrix
    projectionMatrix = ortho(left, right, bottom, ytop, near, far);

    // Rotations from the sliders
    gRotate(RZ,0,0,1) ;
    gRotate(RY,0,1,0) ;
    gRotate(RX,1,0,0) ;


    // set all the matrices
    setAllMatrices() ;

    var curTime ;
    if( animFlag )
    {
        curTime = (new Date()).getTime() /1000 ;
        if( resetTimerFlag ) {
            prevTime = curTime ;
            resetTimerFlag = false ;
        }
        TIME = TIME + curTime - prevTime ;
        prevTime = curTime ;
    }
    //sea floor
    gPush();{
      gScale(canvas.width,.5,1);
      gTranslate(0,-12.4,0);
      drawCube();
    }
    gPop();
    //diver torso
    gPush() ;
    {
        gTranslate(0,-.4*Math.cos(TIME),0) ;
        gTranslate(4,-3,0);
        setColor(vec4(0.0,0.0,1.0,1.0)) ;
        gRotate(340,0,1,0);

        gScale(.5,1,.5);
        drawCube() ;
    }
    gPop() ;

    //diver head
    gPush();{
        setColor(vec4(0.0,0.0,1.0,1.0)) ;
        gScale(.3,.3,.3);
        gTranslate(0,-1.3*Math.cos(TIME),0) ;
        gTranslate(13.25,-5.7,0);
        drawSphere();
    }
    gPop();
    //diver left thigh
    gPush();{
      gTranslate(0,-.4*Math.cos(TIME),0) ;
      gTranslate(3.7,-4.5,0) ;
      setColor(vec4(0.0,0.0,1.0,1.0)) ;
      gRotate(340,0,1,0);
      gRotate(10,1,0,0);
      gRotate(340,0,1,0);
      gScale(.1,.5,.1);
      drawCube() ;
    }
    gPop();
    //diver right thigh
  gPush();{
      gTranslate(0,-.4*Math.cos(TIME),0) ;
      gTranslate(4.3,-4.5,0) ;
      setColor(vec4(0.0,0.0,1.0,1.0)) ;
      gRotate(340,0,1,0);
      gRotate(10,1,0,0);
      gRotate(340,0,1,0);
      gScale(.1,.5,.1);
      drawCube() ;
    }
    gPop();
    //diver RIGHT calf
    gPush();{
      gTranslate(0,-.4*Math.cos(TIME),0) ;
      gTranslate(3.95,-5.0,-.5);
      setColor(vec4(0.0,0.0,1.0,1.0));
      gRotate(90,0,0,1);
      gRotate(240,1,0,0);
      gRotate(20*Math.sin(TIME),0,0,1);
      gScale(.1,.5,.1);
      drawCube() ;
    }
    gPop();

    //diver LEFT calf
    gPush();{
      gTranslate(0,-.4*Math.cos(TIME),0) ;
      gTranslate(4.5,-5.0,-.5);
      setColor(vec4(0.0,0.0,1.0,1.0));
      gRotate(90,0,0,1);
      gRotate(240,1,0,0);
      gRotate(-20*Math.sin(TIME),0,0,1);
      gScale(.1,.5,.1);
      drawCube() ;
    }
    gPop();

    //fish torso
   gPush()
    {
        gTranslate(-3*Math.sin(TIME), 0 , 3*Math.cos(TIME));
        gRotate(-58*TIME,0,1,0);
        gTranslate(0,-5,1) ;
        gScale(2,.5,.5);
        gRotate(90,0,1,0);
        setColor(vec4(1,0.0,0.0,1.0)) ;
        drawCone() ;
    }
    gPop() ;

    //fish mouth
    gPush();{
      gTranslate(-3*Math.sin(TIME),0 , 3*Math.cos(TIME));
      gRotate(-58*TIME,0,1,0);
      gTranslate(-1.24,-5,1.0);
      gRotate(270,0,1,0);
      gScale(.5,.5,.5);
      setColor(vec4(1.0,0.0,1.0,1.0)) ;
      drawCone();
    }
    gPop();

    //upper rear fin
    gPush();{
      gTranslate(-3*Math.sin(TIME),0 , 3*Math.cos(TIME));
      gRotate(-58*TIME,0,1,0);
      gTranslate(1.2,-4.6,1);
      gRotate(90,0,1,0);
      gRotate(300,1,0,0);
      gScale(.1,.1,1);
      setColor(vec4(1,0.0,0.0,1.0)) ;
      drawCone();
    }
    gPop();

    //lower rear fin
    gPush();{
      gTranslate(-3*Math.sin(TIME),0 , 3*Math.cos(TIME));
      gRotate(-58*TIME,0,1,0);
      gTranslate(1.1,-5.2,1);
      gRotate(90,0,1,0);
      gRotate(45,1,0,0);
      gScale(.1,.1,.5);
      setColor(vec4(1,0.0,0.0,1.0)) ;
      drawCone();
    }
    gPop();

    //eyeball right
    gPush();{
      gTranslate(-3*Math.sin(TIME),0 , 3*Math.cos(TIME));
      gRotate(-58*TIME,0,1,0);
      setColor(vec4(1.0,1.0,1.0));
      gScale(.1,.1,.1);
      gTranslate(-12,-47,8);
      drawSphere();
    }
    //eyeball left
      gPop();
      gPush();{
      gTranslate(-3*Math.sin(TIME),0 , 3*Math.cos(TIME));
      gRotate(-58*TIME,0,1,0);
      setColor(vec4(1.0,1.0,1.0));
      gScale(.1,.1,.1);
      gTranslate(-12,-47,12);
      drawSphere();
    }
    //pupil right
      gPop();
      gPush();{
      gTranslate(-3*Math.sin(TIME),0 , 3*Math.cos(TIME));
      gRotate(-58*TIME,0,1,0);
      setColor(vec4(0.0,0.0,0.0));
      gScale(.05,.05,.05);
      gTranslate(-25.5,-94,24);
      drawSphere();
    }
    gPop();
    //pupil left
    gPush();{
      gTranslate(-3*Math.sin(TIME),0 , 3*Math.cos(TIME));
      gRotate(-58*TIME,0,1,0);
      setColor(vec4(0.0,0.0,0.0));
      gScale(.05,.05,.05);
      gTranslate(-25.5,-94,16);
      drawSphere();
    }
    gPop();

    //big rock
    gPush();{
      setColor(vec4(.3,.3,.3));
      gScale(.6,.6,.6);
      gTranslate(0,-8.5,-.5);
      drawSphere();
    }

    gPop();
    //small rock
    gPush();{
      setColor(vec4(.3,.3,.3));
      gScale(.4,.4,.4);
      gTranslate(-3,-13.2,-.5);
      drawSphere();
    }
    gPop();
    //contstructing the seaweed
    for( i = 0; i < 3; i++){
      var offsetx = 0;
      var offsety = 0;
      var rotateoffset = -20;
      if(i == 1){
        offsetx = .5;
        offsety = .3;
      }
      if(i==2){
        offsetx = 1;
      }
    gPush();{
      gTranslate(-.5 + offsetx,-4.7 + offsety,-.3);
      drawWeed();
    }
    gPop();
    gPush();{
      gTranslate(-.5 + offsetx,-4.3 + offsety,-.3);
      gTranslate(.1*Math.sin(TIME),0,0);
      gRotate(rotateoffset*Math.sin(TIME),0,0,1);
      drawWeed();
    }
    gPop();
    gPush();{
      gTranslate(-.5 + offsetx,-3.9 + offsety,-.3);
      gTranslate(.2*Math.sin(TIME),0,0);
      gRotate(rotateoffset*Math.sin(TIME),0,0,1);
      drawWeed();
    }
    gPop();
    gPush();{
      gTranslate(-.5 + offsetx,-3.5 + offsety,-.3);
      gTranslate(.3*Math.sin(TIME),0,0);
      gRotate(rotateoffset*Math.sin(TIME),0,0,1);
      drawWeed();
    }
    gPop();

    gPush();{
      gTranslate(-.5 + offsetx,-3.1 + offsety,-.3);
      gTranslate(.4*Math.sin(TIME),0,0);
      gRotate(rotateoffset*Math.sin(TIME),0,0,1);
      drawWeed();
    }
    gPop();

    gPush();{
     gTranslate(-.5 + offsetx,-2.72 + offsety,-.3);
     gTranslate(.5*Math.sin(TIME),0,0);
     gRotate(rotateoffset*Math.sin(TIME),0,0,1);
     drawWeed();
    }
    gPop();
    gPush();{
      gTranslate(-.5 + offsetx,-2.32 + offsety,-.3);
      gTranslate(.6*Math.sin(TIME),0,0);
      gRotate(rotateoffset*Math.sin(TIME),0,0,1);
      drawWeed();
    }
    gPop();
    gPush();{
      gTranslate(-.5 + offsetx,-1.92 + offsety,-.3);
      gTranslate(.7*Math.sin(TIME),0,0);
      gRotate(rotateoffset*Math.sin(TIME),0,0,1);
      drawWeed();
    }
    gPop();
    gPush();{
      gTranslate(-.5 + offsetx,-1.53 + offsety,-.3);
      gTranslate(.8*Math.sin(TIME),0,0);
      gRotate(rotateoffset*Math.sin(TIME),0,0,1);
      drawWeed();
    }
    gPop();
    gPush();{
      gTranslate(-.5 + offsetx,-1.13 + offsety,-.3);
      gTranslate(.9*Math.sin(TIME),0,0);
      gRotate(rotateoffset*Math.sin(TIME),0,0,1);
      drawWeed();
    }
    gPop();
    
  }
    if( animFlag )
        window.requestAnimFrame(render);
}
//draws a simple elipsis for each piece of the seaweed
function drawWeed(){
  setColor(vec4(0.0,1.0,0.3,1.0));
  gScale(.1,.2,.1);

  drawSphere();
}

// A simple camera controller which uses an HTML element as the event
// source for constructing a view matrix. Assign an "onchange"
// function to the controller as follows to receive the updated X and
// Y angles for the camera:
//
//   var controller = new CameraController(canvas);
//   controller.onchange = function(xRot, yRot) { ... };
//
// The view matrix is computed elsewhere.
function CameraController(element) {
	var controller = this;
	this.onchange = null;
	this.xRot = 0;
	this.yRot = 0;
	this.scaleFactor = 3.0;
	this.dragging = false;
	this.curX = 0;
	this.curY = 0;

	// Assign a mouse down handler to the HTML element.
	element.onmousedown = function(ev) {
		controller.dragging = true;
		controller.curX = ev.clientX;
		controller.curY = ev.clientY;
	};

	// Assign a mouse up handler to the HTML element.
	element.onmouseup = function(ev) {
		controller.dragging = false;
	};

	// Assign a mouse move handler to the HTML element.
	element.onmousemove = function(ev) {
		if (controller.dragging) {
			// Determine how far we have moved since the last mouse move
			// event.
			var curX = ev.clientX;
			var curY = ev.clientY;
			var deltaX = (controller.curX - curX) / controller.scaleFactor;
			var deltaY = (controller.curY - curY) / controller.scaleFactor;
			controller.curX = curX;
			controller.curY = curY;
			// Update the X and Y rotation angles based on the mouse motion.
			controller.yRot = (controller.yRot + deltaX) % 360;
			controller.xRot = (controller.xRot + deltaY);
			// Clamp the X rotation to prevent the camera from going upside
			// down.
			if (controller.xRot < -90) {
				controller.xRot = -90;
			} else if (controller.xRot > 90) {
				controller.xRot = 90;
			}
			// Send the onchange event to any listener.
			if (controller.onchange != null) {
				controller.onchange(controller.xRot, controller.yRot);
			}
		}
	};
}
