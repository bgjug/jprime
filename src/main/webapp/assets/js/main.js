(function($) {
  
  "use strict";  

  $(window).on('load', function() {
  /*Page Loader active
    ========================================================*/
    $('#preloader').fadeOut();

  // Sticky Nav
    $(window).on('scroll', function() {
        if ($(window).scrollTop() > 200) {
            $('.scrolling-navbar').addClass('top-nav-collapse');
        } else {
            $('.scrolling-navbar').removeClass('top-nav-collapse');
        }
    });

    /* ==========================================================================
       countdown timer
       ========================================================================== */
     jQuery('#clock').countdown('2020/05/27 08:00:00',function(event){
      var $this=jQuery(this).html(event.strftime(''
      +'<div class="time-entry days"><span>%-D</span> Days</div> '
      +'<div class="time-entry hours"><span>%H</span> Hours</div> '
      +'<div class="time-entry minutes"><span>%M</span> Minutes</div> '
      +'<div class="time-entry seconds"><span>%S</span> Seconds</div> '));
    });

    /* slicknav mobile menu active  */
    $('.mobile-menu').slicknav({
        prependTo: '.navbar-header',
        parentTag: 'liner',
        allowParentLinks: true,
        duplicate: true,
        label: '',
        closedSymbol: '<i class="icon-arrow-right"></i>',
        openedSymbol: '<i class="icon-arrow-down"></i>',
      });

      /* WOW Scroll Spy
    ========================================================*/
     var wow = new WOW({
      //disabled for mobile
        mobile: false
    });
    wow.init();

    /* Nivo Lightbox 
    ========================================================*/
    $('.lightbox').nivoLightbox({
        effect: 'fadeScale',
        keyboardNav: true,
      });

    /* Nav Menu Hover active
    ========================================================*/
    $(".nav > li:has(ul)").addClass("drop");
    $(".nav > li.drop > ul").addClass("dropdown");
    $(".nav > li.drop > ul.dropdown ul").addClass("sup-dropdown");

    /* Counter
    ========================================================*/
    $('.counterUp').counterUp({
     delay: 10,
     time: 1000
    });

    /* Client Owl Carousel
    ========================================================*/
     var owl = $("#client-logo");
      owl.owlCarousel({
          navigation: false,
          pagination: false,
          items:5,
          itemsTablet:3,
          stagePadding:90,
          smartSpeed:450,
          itemsDesktop : [1199,4],
          itemsDesktopSmall : [980,3],
          itemsTablet: [768,3],
          itemsTablet: [767,2],
          itemsTabletSmall: [480,2],
          itemsMobile : [479,1],
      });

      /* Testimonials Carousel active
      ========================================================*/
      var owl = $(".testimonials-carousel");
        owl.owlCarousel({
          navigation: false,
          pagination: true,
          slideSpeed: 1000,
          stopOnHover: true,
          autoPlay: true,
          items: 1,
          itemsDesktop : [1199,1],
          itemsDesktopSmall : [980,1],
          itemsTablet: [768,1],
          itemsTablet: [767,1],
          itemsTabletSmall: [480,1],
          itemsMobile : [479,1],
        });

      /* Touch Owl Carousel active
      ========================================================*/
      var owl = $(".touch-slider");
        owl.owlCarousel({
          navigation: true,
          pagination: false,
          slideSpeed: 1000,
          stopOnHover: true,
          autoPlay: true,
          items: 1,
          itemsDesktopSmall: [1024, 1],
          itemsTablet: [600, 1],
          itemsMobile: [479, 1]
        });

      var newProducts = $('#new-products');
      newProducts.find('.owl-prev').html('<i class="fa fa-angle-left"></i>');
      newProducts.find('.owl-next').html('<i class="fa fa-angle-right"></i>');

      var testiCarousel = $('.testimonials-carousel');
      testiCarousel.find('.owl-prev').html('<i class="fa fa-angle-left"></i>');
      testiCarousel.find('.owl-next').html('<i class="fa fa-angle-right"></i>');

      var touchSlider = $('.touch-slider');
      touchSlider.find('.owl-prev').html('<i class="fa fa-angle-left"></i>');
      touchSlider.find('.owl-next').html('<i class="fa fa-angle-right"></i>');

      /* owl Carousel active
      ========================================================*/   
      var owl;
      $(window).on('load', function() {
          owl = $("#owl-demo");
          owl.owlCarousel({
              navigation: false, // Show next and prev buttons
              slideSpeed: 300,
              paginationSpeed: 400,
              singleItem: true,
              afterInit: afterOWLinit, // do some work after OWL init
              afterUpdate : afterOWLinit
          });

          function afterOWLinit() {
              // adding A to div.owl-page
              $('.owl-controls .owl-page').append('<a class="item-link" />');
              var pafinatorsLink = $('.owl-controls .item-link');
              /**
               * this.owl.userItems - it's your HTML <div class="item"><img src="http://www.ow...t of us"></div>
               */
              $.each(this.owl.userItems, function (i) {
                $(pafinatorsLink[i])
                    // i - counter
                    // Give some styles and set background image for pagination item
                    .css({
                        'background': 'url(' + $(this).find('img').attr('src') + ') center center no-repeat',
                        '-webkit-background-size': 'cover',
                        '-moz-background-size': 'cover',
                        '-o-background-size': 'cover',
                        'background-size': 'cover'
                    })
                    // set Custom Event for pagination item
                    .on('click',function () {
                        owl.trigger('owl.goTo', i);
                    });

              });
               // add Custom PREV NEXT controls
              $('.owl-pagination').prepend('<a href="#prev" class="prev-owl"/>');
              $('.owl-pagination').append('<a href="#next" class="next-owl"/>');
              // set Custom event for NEXT custom control
              $(".next-owl").on('click',function () {
                  owl.trigger('owl.next');
              });
              // set Custom event for PREV custom control
              $(".prev-owl").on('click',function () {
                  owl.trigger('owl.prev');
              });
          }
      });

        /* Back Top Link active
        ========================================================*/
          var offset = 200;
          var duration = 500;
          $(window).scroll(function() {
            if ($(this).scrollTop() > offset) {
              $('.back-to-top').fadeIn(400);
            } else {
              $('.back-to-top').fadeOut(400);
            }
          });

          $('.back-to-top').on('click',function(event) {
            event.preventDefault();
            $('html, body').animate({
              scrollTop: 0
            }, 600);
            return false;
          });

          /**
        * stellar js
        */
          $.stellar({
            horizontalScrolling: false,
            verticalOffset: 40
          });


  });      

}(jQuery));
